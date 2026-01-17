package de.uprank.server.proxy.module.api;

import de.uprank.server.proxy.module.api.context.ModuleContext;

public interface VelocityModule {

    void onEnable(ModuleContext ctx);
    void onDisable();

}
