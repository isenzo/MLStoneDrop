package me.isenzo.mlstonedrop.commands;

import me.isenzo.mlstonedrop.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;

public class UpdateValueCommand implements CommandExecutor {
    private final Main plugin;

    public UpdateValueCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Sprawdzenie uprawnień
        if (!sender.hasPermission("mldrop.admin")) {
            sender.sendMessage(ChatColor.RED + "§8[§6MLDrop§8] §cNie masz uprawnień do użycia tej komendy.");
            return true;
        }

        // Sprawdzenie, czy komenda to 'admin'
        if (args.length < 2 || !args[0].equalsIgnoreCase("admin")) {
            sender.sendMessage("§8[§6MLDrop§8] §cNieznana komenda. Wpisz: §e/mldrop admin help");
            return true;
        }

        if (args[1].equalsIgnoreCase("update")) {
            return handleUpdateCommand(sender, args);
        }

        if (args[1].equalsIgnoreCase("update-name")) {
            return handleUpdateNameCommand(sender, args);
        }

        if (args[1].equalsIgnoreCase("update-chance")) {
            return handleUpdateChanceCommand(sender, args);
        }

        if (args[1].equalsIgnoreCase("del")) {
            return handleDelCommand(sender, args);
        }

        if (args[1].equalsIgnoreCase("add")) {
            return handleAddCommand(sender, args);
        }

        if (args[1].equalsIgnoreCase("help")) {
            return handleHelpCommand(sender);
        }

        if(args[1].equalsIgnoreCase("list")){
            return handleListCommand(sender);
        }

        if(args[1].equalsIgnoreCase("reload")){
            return handleReloadCommand(sender);
        }

        sender.sendMessage("§8[§6MLDrop§8] §cNieznana komenda. Wpisz: §e/mldrop admin help");
        return true;
    }

    private boolean handleAddCommand(CommandSender sender, String[] args) {
        String materialName = args[2].toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage("§8[§6MLDrop§8] §7Nie znaleziono materiału: §c" + materialName);
            return true;
        }

        // Dodaj przedmiot do konfiguracji z domyślną szansą 0%
        plugin.getConfig().set("drops." + materialName + ".default-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".vip-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".svip-chance", 0.0);
        plugin.getConfig().set("drops." + materialName + ".sponsor-chance", 0.0);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.GREEN + "§8[§6MLDrop§8] §7Dodano przedmiot §e" + materialName + " §7szana domyślnie ustawiona na §a0%");
        return true;
    }

    private boolean handleDelCommand(CommandSender sender, String[] args) {
        String materialName = args[2].toUpperCase();

        if (!plugin.getConfig().contains("drops." + materialName)) {
            sender.sendMessage(ChatColor.RED + "§8[§6MLDrop§8] §7Nie znaleziono przedmiotu: §c" + materialName + " §7w konfiguracji.");
            return true;
        }

        plugin.getConfig().set("drops." + materialName, null);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.GREEN + "§8[§6MLDrop§8] §7Usunięto przedmiot §c" + materialName + " §7z konfiguracji.");
        return true;
    }

    private boolean handleUpdateCommand(CommandSender sender, String[] args) {
        if (args.length < 6) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin update §f<materiał> <grupa> <nowa_szansa> <nowa_nazwa>");
            return true;
        }

        String materialName = args[2].toUpperCase();
        String group = args[3];
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
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

        plugin.getConfig().set(configPathChance, newChance);
        plugin.getConfig().set(configPathName, newName);
        plugin.saveConfig();

        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano szansę dropu dla §a" + materialName + " §7w grupie §a" + group + " §7do §a" + (newChance * 100) + "% §7i nazwę na §a" + newName + "§7.");
        return true;
    }

    private boolean handleUpdateNameCommand(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "§8[§6MLDrop§8] §cNiepoprawne użycie komendy. Poprawne użycie: /mldrop admin update-name <materiał> <nowa_nazwa>");
            return true;
        }

        String materialName = args[2].toUpperCase();
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            sender.sendMessage(ChatColor.RED + "§8[§6MLDrop§8] §cNie znaleziono materiału: §6" + materialName);
            return true;
        }

        String newName = String.join(" ", Arrays.copyOfRange(args, 3, args.length));

        String configPathName = "drops." + materialName + ".item_name";
        plugin.getConfig().set(configPathName, newName);
        plugin.saveConfig();

        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano nazwę dla §a" + materialName + " §7na §a" + newName + "§7.");
        return true;
    }

    private boolean handleUpdateChanceCommand(CommandSender sender, String[] args) {
        if (args.length < 4) {
            sender.sendMessage("§8[§6MLDrop§8] §cNiepoprawne użycie komendy.");
            sender.sendMessage("§e» §7/mldrop admin update-chance §f<materiał> <grupa> <nowa_szansa>");
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

        String configPathChance = "drops." + materialName + "." + group;
        plugin.getConfig().set(configPathChance, newChance);
        plugin.saveConfig();

        String chanceMessage = String.format("§a" + Locale.ENGLISH, "%.2f", (newChance * 100) + "%");
        sender.sendMessage("§8[§6MLDrop§8] §7Zaktualizowano szansę dropu dla §a" + materialName + " §7w grupie §a" + group + " §7do " + String.format("%.2f", newChance * 100) + "%");
        return true;
    }

    private boolean handleHelpCommand(CommandSender sender) {
        sender.sendMessage("§8==============================================================");
        sender.sendMessage("§8[§6MLDrop§8] §6Lista dostępnych komend §cAdmina: ");
        sender.sendMessage("§e» §7/mldrop admin update §f<materiał> <ranga> <szansa> <nazwa>");
        sender.sendMessage("§e» §7/mldrop admin update-chance §f<materiał> <ranga> <szansa>");
        sender.sendMessage("§e» §7/mldrop admin update-name §f<materiał> <nazwa>");
        sender.sendMessage("§e» §7/mldrop admin add §f<materiał>");
        sender.sendMessage("§e» §7/mldrop admin del §f<materiał>");
        sender.sendMessage("§e» §7/mldrop admin list");
        sender.sendMessage("§e» §7/mldrop admin reload");
        sender.sendMessage("§8==============================================================");

        return true;
    }

    private boolean handleListCommand(CommandSender sender){

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
                sender.sendMessage("§7» §avip-chance: §f"  + vipChance + "%");
                sender.sendMessage("§7» §asvip-chance: §f" + svipChance + "%");
                sender.sendMessage("§7» §asponsor-chance: §f" + sponsorChance + "%");
                sender.sendMessage("§8================================================");
            }
        }
        return true;
    }

    public boolean handleReloadCommand(CommandSender sender){
        Player player = (Player) sender;
        plugin.getConfigManager().reloadConfig();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8[§6MLDrop§8]§c zaktualizowano plik konfiguracyjny."));

        return true;
    }

}
