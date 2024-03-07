package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class AddCommand implements CommandAction {

    private final Main plugin;

    public boolean handleAddCommand(CommandSender sender, String[] args) {
        String materialName = args[2].toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §7Nie znaleziono materiału: §c" + materialName);
            return true;
        }

        plugin.getConfig().set("drops." + materialName + ".default-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".vip-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".svip-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".sponsor-chance", 0.0);
        plugin.saveConfig();

        sender.sendMessage("§8[§6MLDrop§8] §7Dodano przedmiot §e" + materialName + " §7szana domyślnie ustawiona na §a0%");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleAddCommand(sender, args);
        return true;
    }
}
