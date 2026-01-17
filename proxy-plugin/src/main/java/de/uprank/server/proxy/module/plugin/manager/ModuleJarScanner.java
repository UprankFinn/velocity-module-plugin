package de.uprank.server.proxy.module.plugin.manager;

import de.uprank.server.proxy.module.api.Module;
import de.uprank.server.proxy.module.api.VelocityModule;

import java.io.IOException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleJarScanner {

    public static Class<? extends VelocityModule> findModuleMainClass(Path jarPath, URLClassLoader urlClassLoader) throws IOException {
        try (JarFile jar = new JarFile(jarPath.toFile())) {
            Enumeration<JarEntry> en = jar.entries();

            while (en.hasMoreElements()) {
                JarEntry e = en.nextElement();
                if (e.isDirectory()) continue;
                String name = e.getName();
                if (!name.endsWith(".class")) continue;

                String className = name.replace('/', '.').substring(0, name.length() - 6);
                try {
                    Class<?> c = Class.forName(className, false, urlClassLoader);

                    if (!VelocityModule.class.isAssignableFrom(c)) continue;
                    if (c.getAnnotation(Module.class) == null) continue;

                    return (Class<? extends VelocityModule>) c;
                } catch (ClassNotFoundException | NoClassDefFoundError ignored) {
                }
            }
        }
        return null;
    }

}
