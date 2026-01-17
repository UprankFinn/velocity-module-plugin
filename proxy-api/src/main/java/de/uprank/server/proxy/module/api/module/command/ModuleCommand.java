package de.uprank.server.proxy.module.api.module.command;

import com.velocitypowered.api.command.CommandSource;

public interface ModuleCommand {

    void execute(CommandSource source, String[] args);

}
