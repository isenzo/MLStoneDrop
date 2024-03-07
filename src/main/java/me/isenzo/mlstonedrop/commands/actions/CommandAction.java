package me.isenzo.mlstonedrop.commands.actions;

import org.bukkit.command.CommandSender;

public interface CommandAction {
    boolean execute(CommandSender sender, String[] args);
}
