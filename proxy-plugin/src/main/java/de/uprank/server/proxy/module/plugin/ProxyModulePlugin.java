package de.uprank.server.proxy.module.plugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.uprank.server.proxy.module.plugin.manager.ModuleManager;
import de.uprank.server.proxy.module.plugin.manager.ModuleWatcher;

import java.nio.file.Path;

import org.slf4j.Logger;

@Plugin(id = "proxy-module-plugin", name = "Proxy Module Plugin", version = "1.0-SNAPSHOT", authors = {"Uprank"})
public class ProxyModulePlugin {

    private final ProxyServer proxyServer;
    private final Logger logger;

    private ModuleManager moduleManager;
    private ModuleWatcher moduleWatcher;

    @Inject
    public ProxyModulePlugin(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;
    }

    @Subscribe
    public void onInitializeProxy(ProxyInitializeEvent event) throws Exception {
        Path baseDir = Path.of("plugins", "ProxyModulePlugin");
        Path modulesDir = baseDir.resolve("modules");
        Path moduleDataDir = baseDir.resolve("module-data");

        this.moduleManager = new ModuleManager(this, this.proxyServer, this.logger, modulesDir, moduleDataDir);
        this.moduleManager.loadAll();

        this.moduleWatcher = new ModuleWatcher(modulesDir, this.moduleManager, this.logger);
        this.moduleWatcher.start();

    }

    @Subscribe
    public void onShutdownProxy(ProxyShutdownEvent event) {
        try {
            if (this.moduleWatcher != null) this.moduleWatcher.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (this.moduleManager != null) this.moduleManager.unloadAll();
    }

}
