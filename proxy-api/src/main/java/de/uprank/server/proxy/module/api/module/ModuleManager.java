package de.uprank.server.proxy.module.api.module;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;

import java.util.concurrent.TimeUnit;

public interface ModuleManager {

    void registerListener(Object listener);
    void unregisterListener(Object listener);

    void registerCommand(CommandMeta meta, Command command);
    void unregisterCommand(CommandMeta meta);
    void unregisterCommandAlias(String alias);

    void scheduleRepeatingTask(Runnable task, long initialDelayTicks, long periodTicks, TimeUnit unit);
    void scheduleOnce(Runnable task, long delayTicks, TimeUnit unit);

    void cleanup();

}
