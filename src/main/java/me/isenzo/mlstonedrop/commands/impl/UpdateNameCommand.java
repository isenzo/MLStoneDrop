package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

@RequiredArgsConstructor
public class UpdateNameCommand implements CommandAction {

    private final Main plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleUpdateNameCommand(sender, args);
        return true;
    }

    public boolean handleUpdateNameCommand(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin update-chance §f<materiał> <grupa> <nowa_szansa>");
            sender.sendMessage("§ePrzykład: » §7/mldrop admin update-name §fRAW_IRON &2Nowa Nazwa");
            return true;
        }

        String materialName = args[2].toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §cNie znaleziono materiału: §6" + materialName);
            return true;
        }

        String newName = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

        String configPathName = "drops." + materialName + ".item_name";
        plugin.getConfig().set(configPathName, newName);
        plugin.saveConfig();

        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano nazwę dla §a" + materialName + " §7na §a" + newName + "§7.");
        return true;
    }
}
