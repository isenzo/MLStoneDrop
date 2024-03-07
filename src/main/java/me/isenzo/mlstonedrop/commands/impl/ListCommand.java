package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

@RequiredArgsConstructor
public class ListCommand implements CommandAction {

    private final Main plugin;

    public boolean handleListCommand(CommandSender sender) {

        ConfigurationSection dropsSection = plugin.getConfig().getConfigurationSection("drops");
        if (dropsSection == null) {
            sender.sendMessage("§8[§6MLDrop§8] §cBrak zdefiniowanych przedmiotów do dropu.");
            return true;
        }

        sender.sendMessage("§7=======§8[§6Lista przedmiotów i szans dla grupy§8]§7=======");
        for (String key : dropsSection.getKeys(false)) {
            Material material = Material.matchMaterial(key);
            if (material != null) {
                double defaultChance = dropsSection.getDouble(key + ".default-chance", 0.0) * 100;
                double vipChance = dropsSection.getDouble(key + ".vip-chance", defaultChance) * 100;
                double svipChance = dropsSection.getDouble(key + ".svip-chance", defaultChance) * 100;
                double sponsorChance = dropsSection.getDouble(key + ".sponsor-chance", defaultChance) * 100;

                // Formatowanie i wysyłanie wiadomości
                sender.sendMessage("§6Nazwa przedmiotu: §c" + material.name());
                sender.sendMessage("§7» §adefault-chance: §f" + defaultChance + "%");
                sender.sendMessage("§7» §avip-chance: §f" + vipChance + "%");
                sender.sendMessage("§7» §asvip-chance: §f" + svipChance + "%");
                sender.sendMessage("§7» §asponsor-chance: §f" + sponsorChance + "%");
                sender.sendMessage("§8================================================");
            }
        }
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleListCommand(sender);
        return true;
    }
}
