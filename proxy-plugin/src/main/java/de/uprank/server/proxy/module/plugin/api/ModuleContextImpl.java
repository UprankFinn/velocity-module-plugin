package de.uprank.server.proxy.module.plugin.api;

import com.velocitypowered.api.proxy.ProxyServer;
import de.uprank.server.proxy.module.api.context.ModuleContext;
import de.uprank.server.proxy.module.api.module.ModuleManager;

import java.nio.file.Path;

public final class ModuleContextImpl implements ModuleContext {

    private final ProxyServer proxyServer;
    private final Path dataFolder;
    private final ModuleManager moduleManager;

    public ModuleContextImpl(ProxyServer proxyServer, Path dataFolder, ModuleManager moduleManager) {
        this.proxyServer = proxyServer;
        this.dataFolder = dataFolder;
        this.moduleManager = moduleManager;
    }

    @Override
    public ProxyServer proxyServer() {
        return this.proxyServer;
    }

    @Override
    public ModuleManager moduleManager() {
        return this.moduleManager;
    }

    @Override
    public Path dataFolder() {
        return this.dataFolder;
    }
}
