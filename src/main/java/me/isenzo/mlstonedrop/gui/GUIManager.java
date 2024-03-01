package me.isenzo.mlstonedrop.gui;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class GUIManager {
    private final HashMap<UUID, Boolean> directDropEnabledForPlayer = new HashMap<>();
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();
    private final Main plugin;

    public GUIManager(Main plugin) {
        this.plugin = plugin;
    }

    public void openDropSettingsGUI(Player player) {
        int itemsPerPage = 18;
        Map<Material, Double> drops = plugin.getConfigManager().getDropChances();
        List<Map.Entry<Material, Double>> dropList = new ArrayList<>(drops.entrySet());

        int totalItems = dropList.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
        int page = 1;

        Inventory gui = Bukkit.createInventory(null, 54, "§aDrop z kamienia - Strona " + page);

        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = grayPane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" ");
            grayPane.setItemMeta(paneMeta);
        }

        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, grayPane);
        }

        int rows = 2; // Liczba środkowych wierszy
        int cols = 20; // Dostępne kolumny w środkowych wierszach po pominięciu dwóch kolumn
        int middleRowStart = 18; // Startujemy od 20, pomijając dwie pierwsze kolumny w 3 wierszu
        int itemsToPlace = Math.min(drops.size(), rows * cols); // Liczba przedmiotów do umieszczenia, ograniczona przez dostępną przestrzeń
        int index = middleRowStart + (cols - itemsToPlace % cols) / 2; // Centrujemy przedmioty w wierszu

        for (Map.Entry<Material, Double> entry : drops.entrySet()) {
            Material material = entry.getKey();
            ItemStack item = new ItemStack(material, 1);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                String itemName = plugin.getConfigManager().getItemName(material);
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
                List<String> lore = new ArrayList<>();
                lore.add("§7Szansa wypadnięcia:");
                lore.add("§7Default: §f" + plugin.getConfigManager().getGroupDefaultChance(material) * 100 + "%");
                lore.add("§6VIP: §a" + plugin.getConfigManager().getGroupVipChance(material) * 100 + "%");
                lore.add("§eSVIP: §b" + plugin.getConfigManager().getGroupSvipChance(material) * 100 + "%");
                lore.add("§3Sponsor: §b" + plugin.getConfigManager().getGroupSponsorChance(material) * 100 + "%");
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            gui.setItem(index++, item);

            if ((index - middleRowStart) % 9 == cols) {
                index += 2;
            }
            if (index >= middleRowStart + rows * 9) {
                break;
            }
        }

        if (totalPages > 1) {
            if (page > 1) {
                ItemStack previousPage = new ItemStack(Material.ARROW);
                ItemMeta previousPageMeta = previousPage.getItemMeta();
                if (previousPageMeta != null) {
                    previousPageMeta.setDisplayName("§cPoprzednia strona");
                    previousPage.setItemMeta(previousPageMeta);
                }
                gui.setItem(0, previousPage);
            }

            if (page < totalPages) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                ItemMeta nextPageMeta = nextPage.getItemMeta();
                if (nextPageMeta != null) {
                    nextPageMeta.setDisplayName("§eNastępna strona");
                    nextPage.setItemMeta(nextPageMeta);
                }
                gui.setItem(8, nextPage);
            }
        }

//        ItemStack cobbleDropItem = new ItemStack(cobbleDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
//        ItemMeta cobbleDropMeta = cobbleDropItem.getItemMeta();
//        if (cobbleDropMeta != null) {
//            cobbleDropMeta.setDisplayName("§eDrop cobblestone");
//            List<String> cobbleDropLore = new ArrayList<>();
//            cobbleDropLore.add(cobbleDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć");
//            cobbleDropMeta.setLore(cobbleDropLore);
//            cobbleDropItem.setItemMeta(cobbleDropMeta);
//        }
//        gui.setItem(52, cobbleDropItem);
//
//        ItemStack directDropItem = new ItemStack(directDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? Material.LIME_CONCRETE : Material.RED_CONCRETE);
//        ItemMeta directDropMeta = directDropItem.getItemMeta();
//        if (directDropMeta != null) {
//            directDropMeta.setDisplayName("§eDrop do ekwipunku");
//            List<String> directDropLore = new ArrayList<>();
//            directDropLore.add(directDropEnabledForPlayer.getOrDefault(player.getUniqueId(), true) ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć");
//            directDropMeta.setLore(directDropLore);
//            directDropItem.setItemMeta(directDropMeta);
//        }
//        gui.setItem(53, directDropItem);

        // Pobierz aktualne ustawienia dla gracza
        boolean directDropEnabled = plugin.getPlayerDropManager().isDirectDropEnabled(player.getUniqueId());
        boolean cobbleDropEnabled = plugin.getPlayerDropManager().isCobbleDropEnabled(player.getUniqueId());

        updateDropItem(gui, 52, cobbleDropEnabled, "§eDrop Cobblestone", "Cobblestone Drop");
        updateDropItem(gui, 53, directDropEnabled, "§eDirect Drop", "Direct drop to Inventory");

        player.openInventory(gui);
    }

    private void updateDropItem(Inventory gui, int slot, boolean isEnabled, String displayName, String loreDescription) {
        Material material = isEnabled ? Material.LIME_CONCRETE : Material.RED_CONCRETE;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Arrays.asList(isEnabled ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć", loreDescription));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }
}