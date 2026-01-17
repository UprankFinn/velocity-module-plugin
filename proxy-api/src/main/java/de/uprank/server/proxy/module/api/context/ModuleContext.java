package de.uprank.server.proxy.module.api.context;

import com.velocitypowered.api.proxy.ProxyServer;
import de.uprank.server.proxy.module.api.module.ModuleManager;

import java.nio.file.Path;

public interface ModuleContext {

    ProxyServer proxyServer();
    ModuleManager moduleManager();
    Path dataFolder();

}
