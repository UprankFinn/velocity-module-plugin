package de.uprank.server.proxy.module.plugin.manager;

import com.velocitypowered.api.proxy.ProxyServer;
import de.uprank.server.proxy.module.api.Module;
import de.uprank.server.proxy.module.api.VelocityModule;
import de.uprank.server.proxy.module.plugin.api.ModuleContextImpl;
import de.uprank.server.proxy.module.plugin.api.ModuleManagerImpl;
import de.uprank.server.proxy.module.plugin.module.LoadedModule;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;


public final class ModuleManager {

    private final Object hostPlugin;
    private final ProxyServer proxy;
    private final Logger logger;

    private final Path modulesDir;
    private final Path moduleDataDir;

    private final Map<String, LoadedModule> loaded = new ConcurrentHashMap<>();

    public ModuleManager(Object hostPlugin, ProxyServer proxy, Logger logger, Path modulesDir, Path moduleDataDir) {
        this.hostPlugin = hostPlugin;
        this.proxy = proxy;
        this.logger = logger;

        this.modulesDir = modulesDir;
        this.moduleDataDir = moduleDataDir;
    }

    public void loadAll() throws Exception {
        Files.createDirectories(this.modulesDir);
        Files.createDirectories(this.moduleDataDir);

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(modulesDir, "*.jar")) {
            for (Path jar : ds) {
                try {
                    loadJar(jar);
                } catch (Exception ex) {
                    logger.error("Failed loading " + jar.getFileName(), ex);
                }
            }
        }

    }

    public synchronized void loadJar(Path jarPath) throws Exception {
        URL url = jarPath.toUri().toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url}, getClass().getClassLoader());

        Class<? extends VelocityModule> main = ModuleJarScanner.findModuleMainClass(jarPath, cl);
        if (main == null) {
            cl.close();
            throw new IllegalStateException("No @ModuleInfo VelocityModule in " + jarPath);
        }

        Module info = main.getAnnotation(Module.class);
        String id = info.id().toLowerCase(Locale.ROOT);

        if (loaded.containsKey(id)) {
            cl.close();
            throw new IllegalStateException("Module id already loaded: " + id);
        }
        Path dataDir = moduleDataDir.resolve(id);
        Files.createDirectories(dataDir);

        var registrar = new ModuleManagerImpl(hostPlugin, proxy);
        var ctx = new ModuleContextImpl(proxy, dataDir, registrar);

        VelocityModule instance = main.getDeclaredConstructor().newInstance();
        instance.onEnable(ctx);

        loaded.put(id, new LoadedModule(id, info, jarPath, cl, instance, ctx));
        logger.info("Loaded module {} v{} ({})", info.name(), info.version(), id);
    }

    public synchronized void unload(String id) {
        id = id.toLowerCase(Locale.ROOT);
        LoadedModule m = loaded.remove(id);
        if (m == null) return;

        try {
            try {
                m.instance.onDisable();
            } catch (Exception ex) {
                logger.warn("onUnload failed for " + id, ex);
            }
            try {
                m.moduleContext.moduleManager().cleanup();
            } catch (Exception ex) {
                logger.warn("cleanupAll failed for " + id, ex);
            }
            try {
                m.classLoader.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            logger.info("Unloaded module {}", id);
        } catch (Exception ex) {
            logger.error("Unload error " + id, ex);
        }
    }

    public synchronized void unloadByJar(Path jarPath) {
        String found = null;
        for (LoadedModule m : loaded.values()) {
            if (m.jarPath.equals(jarPath)) {
                found = m.id;
                break;
            }
        }
        if (found != null) unload(found);
    }

    public synchronized void reloadJar(Path jarPath) {
        unloadByJar(jarPath);
        try {
            loadJar(jarPath);
        } catch (Exception ex) {
            logger.error("Reload failed for " + jarPath.getFileName(), ex);
        }
    }

    public synchronized void unloadAll() {
        for (String id : new ArrayList<>(loaded.keySet())) unload(id);
    }

}
