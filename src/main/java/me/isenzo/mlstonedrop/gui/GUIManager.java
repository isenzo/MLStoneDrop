package me.isenzo.mlstonedrop.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.config.DropChance;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

@RequiredArgsConstructor
public class GUIManager {

    private final Main plugin;

    public void openDropSettingsGUI(Player player, int currentPage) {

        Map<Material, DropChance> drops = plugin.getConfigManager().getDropChances();
        int totalItems = drops.size();
        int itemsPerPage = 18;
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        Inventory gui = Bukkit.createInventory(null, 9 * 6, "§aDrop z kamienia - Strona " + currentPage);

        fillBackground(gui);

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        List<Material> materials = new ArrayList<>(drops.keySet());
        for (int i = startIndex; i < endIndex; i++) {
            Material material = materials.get(i);
            addItemToGui(gui, material, i - startIndex + 18, player);
        }

        addButtonsToManipulateDirectDropAndCobbleDropFromStone(gui, player);
        addNavigationArrows(gui, currentPage, totalPages);

        player.openInventory(gui);
    }


    private void addButtonsToManipulateDirectDropAndCobbleDropFromStone(Inventory gui, Player player) {
        boolean directDropEnabled = plugin.getPlayerDropManager().directDrop(player.getUniqueId());
        boolean cobbleDropEnabled = plugin.getPlayerDropManager().cobbleDropFromStone(player.getUniqueId());

//        updateDropItem(gui, 52, cobbleDropEnabled, "§eDrop cobblestone");
//        updateDropItem(gui, 53, directDropEnabled, "§eDrop do ekwipunku");
        createUpdateDropItemButtons(gui, cobbleDropEnabled, "§eDrop cobblestone", 52);
        createUpdateDropItemButtons(gui, directDropEnabled, "§eDrop do ekwipunku", 53);
    }

    private void addNavigationArrows(Inventory gui, int currentPage, int totalPages) {
        String leftArrowBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cm" +
                "UvOTIxMjE3ZDdmZWU5ZmFmMmZiYzc0MWUwYWRiNWRlNmE4NGVhN2Q1NTM2Y2JjY2QxZGJjOGEzMjNmOTViOTkwZCJ9fX0=";
        String rightArrowBase64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cm" +
                "UvMjc2YjM5NmEzNDZmYmQ1ZGM5OGRmOWMwNjA5N2JiNTkxZmVmZDc2OGVkNWIzYmI5MGJiMzJhZjg1ZWI5MTQwNiJ9fX0=";

        if (currentPage > 1) {
//            ItemStack previousPage = new ItemStack(Material.ARROW);
            ItemStack previousPage = createSkullWithTexture(leftArrowBase64, "§cPoprzednia strona");
            ItemMeta previousPageMeta = previousPage.getItemMeta();
            if (previousPageMeta != null) {
                previousPageMeta.setDisplayName("§cPoprzednia strona");
                previousPage.setItemMeta(previousPageMeta);
            }
            gui.setItem(0, previousPage);
        }
        if (currentPage < totalPages) {
//            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemStack nextPage = createSkullWithTexture(rightArrowBase64, "§eNastępna strona");
            ItemMeta nextPageMeta = nextPage.getItemMeta();
            if (nextPageMeta != null) {
                nextPageMeta.setDisplayName("§eNastępna strona");
                nextPage.setItemMeta(nextPageMeta);
            }
            gui.setItem(8, nextPage);
        }
    }

    public ItemStack createSkullWithTexture(String texture, String displayName) {
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        if (skullMeta == null) return head;

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));

        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skullMeta.setDisplayName(displayName);
        head.setItemMeta(skullMeta);

        return head;
    }

    private void addItemToGui(Inventory gui, Material material, int index, Player player) {
        String playerGroup = plugin.getApi().getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        if (Objects.nonNull(meta)) {
            String itemName = plugin.getConfigManager().getItemName(material);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
            List<String> lore = new ArrayList<>();
            lore.add("§7Szansa wypadnięcia:");
            lore.add("");
            lore.add("§9===============================");
            lore.add(formatDropChance("Default", plugin.getConfigManager().getGroupDefaultChance(material), playerGroup.equals("default")));
            lore.add(formatDropChance("VIP", plugin.getConfigManager().getGroupDefaultChance(material), playerGroup.equals("vip")));
            lore.add(formatDropChance("SVIP", plugin.getConfigManager().getGroupDefaultChance(material), playerGroup.equals("svip")));
            lore.add(formatDropChance("Sponsor", plugin.getConfigManager().getGroupDefaultChance(material), playerGroup.equals("sponsor")));
            lore.add("§9===============================");
            lore.add("§7min y: §a" + plugin.getConfigManager().getMinY(material));
            lore.add("§7max y: §c" + plugin.getConfigManager().getMaxY(material));
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        gui.setItem(index, item);
    }

    private String formatDropChance(String groupName, double chance, boolean isPlayerGroup) {
        String arrow = isPlayerGroup ? " §8« §3Twój drop" : "";
        return String.format("§%s%s: §a%.2f%%%s", getGroupColor(groupName), groupName, chance * 100, arrow);
    }

    private String getGroupColor(String groupName) {
        switch (groupName.toLowerCase()) {
            case "vip":
                return "6";
            case "svip":
                return "e";
            case "sponsor":
                return "3";
            default:
                return "7";
        }
    }

    private void updateDropItem(Inventory gui, int slot, boolean enable, String displayName) {
        Material material = enable ? Material.LIME_CONCRETE : Material.RED_CONCRETE;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(Collections.singletonList(enable ? "§7Kliknij, aby §cwyłączyć" : "§7Kliknij, aby §awłączyć"));
            item.setItemMeta(meta);
        }
        gui.setItem(slot, item);
    }

    private void createUpdateDropItemButtons(Inventory gui, boolean enable, String displayName, int slot) {
        String greenBall = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv" +
                "MzI5OWVjNGQxOGEwODAwMzQzMjhiNjY3ZWZiOTVkNzA5M2QxOWI1YmUxYWM5MWI3MDAwZGYwZjE1ZWRkZjgwYSJ9fX0=";

        String redBall = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2" +
                "U2MjMyMTFiZGE1ZGQ1YWI5ZTc2MDMwZjg2YjFjNDczMGI5ODg3MjZlZWY2YTNhYjI4YWExYzFmN2Q4NTAifX19";

        if (enable) {
            ItemStack turnOff = createSkullWithTexture(greenBall, displayName);
            ItemMeta meta = turnOff.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                meta.setLore(Collections.singletonList("§7Kliknij aby §cWyłączyć"));
                turnOff.setItemMeta(meta);
            }
            gui.setItem(slot, turnOff);
        } else {
            ItemStack turnOn = createSkullWithTexture(redBall, displayName);
            ItemMeta meta = turnOn.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(displayName);
                meta.setLore(Collections.singletonList("§7Kliknij aby §aWłączyć"));
                turnOn.setItemMeta(meta);
            }
            gui.setItem(slot, turnOn);
        }

    }


    private void fillBackground(Inventory gui) {
        ItemStack grayPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta paneMeta = grayPane.getItemMeta();
        if (paneMeta != null) {
            paneMeta.setDisplayName(" ");
            grayPane.setItemMeta(paneMeta);
        }
        for (int i = 0; i < gui.getSize(); i++) {
            gui.setItem(i, grayPane);
        }
    }

}