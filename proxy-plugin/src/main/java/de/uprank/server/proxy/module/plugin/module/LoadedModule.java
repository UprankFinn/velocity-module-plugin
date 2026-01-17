package de.uprank.server.proxy.module.plugin.module;

import de.uprank.server.proxy.module.api.Module;
import de.uprank.server.proxy.module.api.VelocityModule;
import de.uprank.server.proxy.module.plugin.api.ModuleContextImpl;

import java.net.URLClassLoader;
import java.nio.file.Path;

public final class LoadedModule {

    public final String id;
    public final Module module;
    public final Path jarPath;
    public final URLClassLoader classLoader;
    public final VelocityModule instance;
    public final ModuleContextImpl moduleContext;

    public LoadedModule(String id, Module module, Path jarPath, URLClassLoader classLoader, VelocityModule instance, ModuleContextImpl moduleContext) {
        this.id = id;
        this.module = module;
        this.jarPath = jarPath;
        this.classLoader = classLoader;
        this.instance = instance;
        this.moduleContext = moduleContext;
    }
}
