package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class DelCommand implements CommandAction {

    private final Main plugin;

    public boolean handleDelCommand(CommandSender sender, String[] args) {
        String materialName = args[2].toUpperCase();

        if (!plugin.getConfig().contains("drops." + materialName)) {
            sender.sendMessage("§8[§6MLDrop§8] §7Nie znaleziono przedmiotu: §c" + materialName + " §7w konfiguracji.");
            return true;
        }

        plugin.getConfig().set("drops." + materialName, null);
        plugin.saveConfig();

        sender.sendMessage("§8[§6MLDrop§8] §7Usunięto przedmiot §c" + materialName + " §7z konfiguracji.");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleDelCommand(sender, args);
        return true;
    }
}
