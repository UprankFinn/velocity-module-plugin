package de.uprank.server.proxy.module.plugin.manager;

import org.slf4j.Logger;

import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class ModuleWatcher implements AutoCloseable {

    private final Path dir;
    private final ModuleManager moduleManager;
    private final Logger logger;

    private WatchService watchService;
    private final Map<Path, ScheduledFuture<?>> debounce = new ConcurrentHashMap<>();

    public ModuleWatcher(Path dir, ModuleManager moduleManager, Logger logger) {
        this.dir = dir;
        this.moduleManager = moduleManager;
        this.logger = logger;
    }

    public void start() throws Exception {
        Files.createDirectories(dir);
        this.watchService = FileSystems.getDefault().newWatchService();
        this.dir.register(this.watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        thread.submit(this::loop);
    }


    private final ExecutorService thread = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "modules-watcher");
        thread.setDaemon(true);
        return thread;
    });

    private final ScheduledExecutorService debounceExec = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "modules-debounce");
        thread.setDaemon(true);
        return thread;
    });

    private void loop() {
        while (!Thread.currentThread().isInterrupted()) {
            WatchKey key;

            try {
                key = this.watchService.take();
            } catch (InterruptedException e) {
                return;
            }

            for (WatchEvent<?> ev : key.pollEvents()) {
                var kind = ev.kind();
                if (kind == OVERFLOW) continue;

                Path rel = (Path) ev.context();
                Path abs = this.dir.resolve(rel);
                if (!abs.getFileName().toString().endsWith(".jar")) continue;

                if (kind == ENTRY_DELETE) {
                    logger.info("Jar deleted -> unloading: {}", abs.getFileName());
                    this.moduleManager.unloadByJar(abs);
                    continue;
                }

                ScheduledFuture<?> old = this.debounce.remove(abs);
                if (old != null) old.cancel(false);

                this.debounce.put(abs, this.debounceExec.schedule(() -> {
                    logger.info("Jar changed -> reloading: {}", abs.getFileName());
                    this.moduleManager.reloadJar(abs);
                }, 800, TimeUnit.MILLISECONDS));
            }
            key.reset();
        }
    }

    @Override
    public void close() throws Exception {
        this.thread.shutdownNow();
        this.debounceExec.shutdownNow();
        if (this.watchService != null) this.watchService.close();
    }

}
