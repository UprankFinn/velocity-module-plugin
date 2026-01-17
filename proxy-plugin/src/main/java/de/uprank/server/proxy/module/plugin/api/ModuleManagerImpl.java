package de.uprank.server.proxy.module.plugin.api;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import de.uprank.server.proxy.module.api.module.ModuleManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ModuleManagerImpl implements ModuleManager {

    private final Object hostPlugin;
    private final ProxyServer proxyServer;

    private final List<Object> listeners = new ArrayList<>();
    private final List<CommandMeta> metas = new ArrayList<>();
    private final List<String> aliases = new ArrayList<>();
    private final List<ScheduledTask> tasks = new ArrayList<>();

    public ModuleManagerImpl(Object hostPlugin, ProxyServer proxyServer) {
        this.hostPlugin = hostPlugin;
        this.proxyServer = proxyServer;
    }

    @Override
    public void registerListener(Object listener) {
        this.proxyServer.getEventManager().register(hostPlugin, listener);
        this.listeners.add(listener);
    }

    @Override
    public void unregisterListener(Object listener) {
        this.proxyServer.getEventManager().unregisterListener(hostPlugin, listener);
        this.listeners.remove(listener);
    }

    @Override
    public void registerCommand(CommandMeta meta, Command command) {
        this.proxyServer.getCommandManager().register(meta, command);
        this.metas.add(meta);
    }

    @Override
    public void unregisterCommand(CommandMeta meta) {
        this.proxyServer.getCommandManager().unregister(meta);
        this.metas.remove(meta);
    }

    @Override
    public void unregisterCommandAlias(String alias) {
        this.proxyServer.getCommandManager().unregister(alias);
        this.aliases.remove(alias);
    }

    @Override
    public void scheduleRepeatingTask(Runnable task, long initialDelayTicks, long periodTicks, TimeUnit unit) {
        ScheduledTask scheduledTask = this.proxyServer.getScheduler().buildTask(hostPlugin, task)
                .delay(initialDelayTicks, unit)
                .repeat(periodTicks, unit).schedule();
        this.tasks.add(scheduledTask);
    }

    @Override
    public void scheduleOnce(Runnable task, long delayTicks, TimeUnit unit) {
        ScheduledTask scheduledTask = this.proxyServer.getScheduler()
                .buildTask(hostPlugin, task)
                .delay(delayTicks, unit).schedule();
        this.tasks.add(scheduledTask);
    }

    @Override
    public void cleanup() {
        for (var t : tasks) {
            try {
                t.cancel();
            } catch (Throwable ignored) {
            }
        }
        tasks.clear();
        for (var meta : new ArrayList<>(metas)) {
            try {
                this.proxyServer.getCommandManager().unregister(meta);
            } catch (Throwable ignored) {
            }
        }
        metas.clear();
        for (var l : new ArrayList<>(listeners)) {
            try {
                this.proxyServer.getEventManager().unregisterListener(hostPlugin, l);
            } catch (Throwable ignored) {
            }
        }
        listeners.clear();
    }
}
