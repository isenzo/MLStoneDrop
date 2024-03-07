package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Objects;

@RequiredArgsConstructor
public class UpdateCommand implements CommandAction {

    private final Main plugin;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleUpdateCommand(sender, args);
        return true;
    }

    public boolean handleUpdateCommand(CommandSender sender, String[] args) {
        if (args.length < 6) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin update §f<materiał> <grupa> <nowa_szansa> <nowa_nazwa>");
            sender.sendMessage("§ePrzykład: » §7/mldrop admin update §fRAW_IRON default 1,2 &6Nazwa przedmiotu");
            return true;
        }

        String materialName = args[2].toUpperCase();

        if (materialNotFoundInConfigManager(materialName, sender)) return true;

        String group = args[3];
        Material material = Material.matchMaterial(materialName);
        if (Objects.isNull(material)) {
            sender.sendMessage("§8[§6MLDrop§8] §cNie znaleziono materiału: §6" + materialName);
            return true;
        }

        double newChance;
        try {
            newChance = Double.parseDouble(args[4]) / 100;
        } catch (NumberFormatException e) {
            sender.sendMessage("§8[§6MLDrop§8] §cPodano niepoprawną wartość szansy: §6" + args[4]);
            return true;
        }

        String newName = String.join(" ", Arrays.copyOfRange(args, 5, args.length));
        String configPathChance = "drops." + materialName + "." + group;
        String configPathName = "drops." + materialName + ".item_name";

        if (!(group.contains("-chance"))) {
            String configPathChanceWithoutPronoun = "drops." + materialName + "." + group + "-chance";
            plugin.getConfig().set(configPathChanceWithoutPronoun, newChance);
            plugin.getConfig().set(configPathName, newName);
            plugin.saveConfig();
        } else {
            plugin.getConfig().set(configPathChance, newChance);
            plugin.getConfig().set(configPathName, newName);
            plugin.saveConfig();

        }

        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano szansę dropu dla §a" + materialName + " §7w grupie §a" + group + " §7do §a" + (newChance * 100) + "% §7i nazwę na §a" + newName + "§7.");
        return true;
    }

    private boolean materialNotFoundInConfigManager(String materialName, CommandSender sender){
        if (!plugin.getConfig().contains("drops." + materialName)) {
            sender.sendMessage("§9=================================================================");
            sender.sendMessage("§8[§6MLDrop§8] §cNie ma takiego przedmiotu w konfiguracji: §6" + materialName);
            sender.sendMessage("§cJeżeli chcesz dodać przedmiot wpisz komendę, lub naciśnij 'Dodaj przedmiot': ");
            sender.sendMessage("");
            executeAddCommandWhenMaterialIsAbsentInConfigFile(materialName, sender);
            sender.sendMessage("§9=================================================================");
            return true;
        }
        return true;
    }

    private void executeAddCommandWhenMaterialIsAbsentInConfigFile(String materialName, CommandSender sender){
        TextComponent addCommandMessage = new TextComponent("§e» §a/mldrop admin add §7" + materialName.toLowerCase() + " §cLub kliknij");

        TextComponent addCommandPart = new TextComponent(ChatColor.GOLD + " Dodaj przedmiot");
        addCommandPart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mldrop admin add " + materialName));
        addCommandPart.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cKliknij, aby dodać nowy przedmiot").italic(true).create()));
        addCommandMessage.addExtra(addCommandPart);

        sender.spigot().sendMessage(addCommandMessage);
    }

}
