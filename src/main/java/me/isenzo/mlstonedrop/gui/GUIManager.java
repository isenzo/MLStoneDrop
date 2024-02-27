package me.isenzo.mlstonedrop.gui;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GUIManager {
    private final HashMap<UUID, Boolean> directDropEnabledForPlayer = new HashMap<>();
    private final HashMap<UUID, Boolean> dropEnabledForPlayer = new HashMap<>();
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();
    private final Main plugin;

    public GUIManager(Main plugin) {
        this.plugin = plugin;
    }

    public void openDropSettingsGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&aDrop z kamienia"));

        // Wypełnienie pustych miejsc szarą szybą
        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = grayPane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" "); // Ustawienie pustej nazwy, aby szyba była "niewidoczna"
            grayPane.setItemMeta(paneMeta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, grayPane);
        }

        Map<Material, Double> drops = plugin.getConfigManager().getDropChances();
        int middleRowStart = 9 * 2; // Początek środkowego wiersza (0-indexed, więc wiersz 3 to indeks 2)
        int index = middleRowStart + (9 - drops.size()) / 2; // Start od środka środkowego wiersza

        for (Map.Entry<Material, Double> entry : drops.entrySet()) {
            Material material = entry.getKey();

            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String itemName = plugin.getConfigManager().getItemName(material);
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));

                List<String> lore = new ArrayList<>();
                lore.add("§7Szansa wypadnięcia: §f" + plugin.getConfigManager().getGroupChance(material, "vip") * 100 + "%");
                lore.add("§7Szansa wypadnięcia: §f" + plugin.getConfigManager().getGroupChance(material, "default") * 100 + "%");
                lore.add("§7Szansa wypadnięcia: §f" + plugin.getConfigManager().getGroupChance(material, "vip") * 100 + "%");
                lore.add("§7Szansa wypadnięcia: §f" + plugin.getConfigManager().getGroupChance(material, "svip") * 100 + "%");
                lore.add("§7Szansa wypadnięcia: §f" + plugin.getConfigManager().getGroupChance(material, "sponsor") * 100 + "%");
                // Dodaj pozostałe lore...

                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            gui.setItem(index, item);
            index++;
            if ((index - middleRowStart) % 9 == 0) {
                index += (9 - drops.size() % 9);
            }
        }

        // Dodajemy blok dla cobble drop
        ItemStack cobbleDropItem = new ItemStack(cobbleDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
        ItemMeta cobbleDropMeta = cobbleDropItem.getItemMeta();
        if (cobbleDropMeta != null) {
            cobbleDropMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aDrop z Cobblestone"));
            List<String> cobbleDropLore = new ArrayList<>();
            cobbleDropLore.add(cobbleDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć");
            cobbleDropMeta.setLore(cobbleDropLore);
            cobbleDropItem.setItemMeta(cobbleDropMeta);
        }
        gui.setItem(36, cobbleDropItem); // Przykładowa pozycja w GUI

        // Dodajemy blok dla direct drop do ekwipunku
        ItemStack directDropItem = new ItemStack(directDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
        ItemMeta directDropMeta = directDropItem.getItemMeta();
        if (directDropMeta != null) {
            directDropMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&aDrop do ekwipunku"));
            List<String> directDropLore = new ArrayList<>();
            directDropLore.add(directDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć");
            directDropMeta.setLore(directDropLore);
            directDropItem.setItemMeta(directDropMeta);
        }
        gui.setItem(44, directDropItem); // Przykładowa pozycja w GUI

        player.openInventory(gui);
    }

    public boolean isDirectDropEnabled(UUID playerId) {
        return directDropEnabledForPlayer.getOrDefault(playerId, true);
    }

    public void toggleDirectDropEnabled(UUID playerId) {
        directDropEnabledForPlayer.put(playerId, !isDirectDropEnabled(playerId));
        saveDropSettings();
    }

    public void saveDropSettings() {
        File file = new File(plugin.getDataFolder(), "playerDropSettings.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        dropEnabledForPlayer.forEach((uuid, enabled) -> config.set(uuid.toString() + ".dropEnabled", enabled));
        cobbleDropEnabledForPlayer.forEach((uuid, enabled) -> config.set(uuid.toString() + ".cobbleDropEnabled", enabled));
        directDropEnabledForPlayer.forEach((uuid, enabled) -> config.set(uuid.toString() + ".directDropEnabled", enabled));

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadDropSettings() {
        File file = new File(plugin.getDataFolder(), "playerDropSettings.yml");
        if (!file.exists()) return;
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            dropEnabledForPlayer.put(uuid, config.getBoolean(key + ".dropEnabled", true));
            cobbleDropEnabledForPlayer.put(uuid, config.getBoolean(key + ".cobbleDropEnabled", true));
            directDropEnabledForPlayer.put(uuid, config.getBoolean(key + ".directDropEnabled", true));
        });
    }
}