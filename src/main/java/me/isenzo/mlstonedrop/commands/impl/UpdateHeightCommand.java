package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class UpdateHeightCommand implements CommandAction {

    private final Main plugin;

    public boolean handleUpdateHeightCommand(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin drop-height <materiał> <min. wysokość (y)> <max wysokość (y)>");
            return true;
        }

        String materialName = args[2].toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §cNie znaleziono materiału: §c" + materialName);
            return true;
        }

        // Próba parsowania wysokości
        try {
            int newMinHeight = Integer.parseInt(args[3]);
            int newMaxHeight = Integer.parseInt(args[4]);

            // Wywołanie walidatora
            if (!plugin.getHeightValidator().validateHeightRange(sender, materialName, args[3], args[4])) {
                sender.sendMessage("§8[§6MLDrop§8] §cPopraw wprowadzone wartości!");
                sender.sendMessage("§7Minimalna wysokość: §a-63§7, maksymalna: §c256");
                return true;
            }
            updateHeightInConfig(materialName, newMinHeight, newMaxHeight);
            sendMessageOnChange(sender, material, newMinHeight, newMaxHeight);
        } catch (NumberFormatException e) {
            sender.sendMessage("§8[§6MLDrop§8] §cPodano niepoprawne wartości dla wysokości.");
        }

        return true;
    }

    private void updateHeightInConfig(String materialName, int newMinHeight, int newMaxHeight) {
        String configPathMinY = "drops." + materialName + ".min-y";
        String configPathMaxY = "drops." + materialName + ".max-y";
        plugin.getConfig().set(configPathMinY, newMinHeight);
        plugin.getConfig().set(configPathMaxY, newMaxHeight);
        plugin.saveConfig();
    }

    private void sendMessageOnChange(CommandSender sender, Material material, int newMinHeight, int newMaxHeight) {
        String itemName = plugin.getConfigManager().getItemName(material);
        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano wysokość dropu dla " + itemName + " §7na §amin: " + newMinHeight + " §7- §cmax: " + newMaxHeight);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleUpdateHeightCommand(sender, args);
        return true;
    }
}
