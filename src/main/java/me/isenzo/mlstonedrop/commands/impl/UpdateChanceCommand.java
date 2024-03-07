package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class UpdateChanceCommand implements CommandAction {

    private final Main plugin;

    public boolean handleUpdateChanceCommand(CommandSender sender, String[] args) {
        if (args.length < 5) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin update-chance §f<materiał> <grupa> <nowa_szansa>");
            sender.sendMessage("§ePrzykład: » §7/mldrop admin update-chance §fRAW_IRON default 2");
            return true;
        }

        String materialName = args[2].toUpperCase();
        String group = args[3];
        double newChance;
        try {
            newChance = Double.parseDouble(args[4]) / 100;
        } catch (NumberFormatException e) {
            sender.sendMessage("§8[§6MLDrop§8] §7Podano niepoprawną wartość szansy: §c" + args[4]);
            return true;
        }

        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §7Nie znaleziono materiału: §c" + materialName);
            return true;
        }

        if (!(group.contains("-chance"))) {
            String configPathWithoutChancePronoun = "drops." + materialName + "." + group + "-chance";
            plugin.getConfig().set(configPathWithoutChancePronoun, newChance);
            plugin.saveConfig();
        } else {
            String configPathChance = "drops." + materialName + "." + group;
            plugin.getConfig().set(configPathChance, newChance);
            plugin.saveConfig();
        }

        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano szansę dropu dla §a" + materialName + " §7w grupie §a" + group + " §7do " + String.format("%.2f", newChance * 100) + "%");
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleUpdateChanceCommand(sender, args);
        return true;
    }
}
