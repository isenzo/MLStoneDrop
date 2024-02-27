package me.isenzo.mlstonedrop.listeners;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class EventListener implements Listener {
    private final Main plugin;

    public EventListener(Main plugin) {
        this.plugin = plugin;
    }

    private void informPlayerAboutDropChange(Player player, boolean isEnabled, String dropType) {
        String message = String.format("§7%s został %s", dropType, isEnabled ? "§awłączony" : "§cwyłączony");
        player.sendMessage(message);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        UUID playerId = player.getUniqueId();
        ItemStack clickedItem = event.getCurrentItem();

        String title = event.getView().getTitle();
        if (!title.equals("§aDrop z kamienia")) {
            return;
        }

        event.setCancelled(true);

        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        ItemMeta meta = clickedItem.getItemMeta();
        String displayName = meta.getDisplayName();

        if (displayName.contains("Cobblestone Drop")) {
            plugin.getPlayerDropManager().toggleCobbleDropEnabled(playerId);
            informPlayerAboutDropChange(player, plugin.getPlayerDropManager().isCobbleDropEnabled(playerId), "Drop Cobblestone");
        } else if (displayName.equals("§6Drop bezpośrednio do ekwipunku")) {
            plugin.getPlayerDropManager().toggleDirectDropEnabled(playerId);
            informPlayerAboutDropChange(player, plugin.getPlayerDropManager().isDirectDropEnabled(playerId), "Drop bezpośrednio do ekwipunku");
        }

        plugin.getPlayerDropManager().saveDropSettings();
        plugin.getGUIManager().openDropSettingsGUI(player);
    }

//    @EventHandler
//    public void onInventoryClick(InventoryClickEvent event) {
//        Player player = (Player) event.getWhoClicked();
//        UUID playerId = player.getUniqueId();
//        ItemStack clickedItem = event.getCurrentItem();
//
//        String title = event.getView().getTitle();
//        if (!title.equals("§aDrop z kamienia")) {
//            return;
//        }
//
//        event.setCancelled(true);
//
//        if (clickedItem == null || !clickedItem.hasItemMeta()) return;
//
//        ItemMeta meta = clickedItem.getItemMeta();
//        String displayName = meta.getDisplayName();
//
//        if (displayName.contains("Cobblestone Drop")) {
//            plugin.getPlayerDropManager().saveDropSettings(); // Zapisz zmiany
//            plugin.getPlayerDropManager().toggleCobbleDropEnabled(playerId);
//            player.sendMessage("§7Drop Cobblestone został " + (plugin.getPlayerDropManager().isCobbleDropEnabled(playerId) ? "§awłączony" : "§cwyłączony"));
//        } else {
//            List<String> lore = meta.getLore();
//            if (lore != null) {
//                for (String line : lore) {
//                    if (line.contains("§aWłącz Drop")) {
//                        plugin.getPlayerDropManager().toggleCobbleDropEnabled(playerId);
//                        player.sendMessage(ChatColor.GREEN + "§aDrop został włączony.");
//                        break;
//                    } else if (line.contains("§cWyłącz Drop")) {
//                        plugin.getPlayerDropManager().toggleCobbleDropEnabled(playerId);
//                        player.sendMessage(ChatColor.RED + "§cDrop został wyłączony.");
//                        break;
//                    }
//                }
//            }
//        }
//
//        if (displayName.equals("§6Drop bezpośrednio do ekwipunku")) {
//            plugin.getPlayerDropManager().toggleDirectDropEnabled(player.getUniqueId());
//            plugin.getPlayerDropManager().saveDropSettings(); // Zapisz zmiany
//            player.sendMessage("§7Drop bezpośrednio do ekwipunku został " + (plugin.getPlayerDropManager().isDirectDropEnabled(player.getUniqueId()) ? "§awłączony" : "§cwyłączony"));
//            plugin.getGUIManager().openDropSettingsGUI(player); // Odśwież GUI
//        } else {
//            List<String> lore = meta.getLore();
//            if (lore != null) {
//                for (String line : lore) {
//                    if (line.contains("§aWłącz Drop")) {
//                        plugin.getPlayerDropManager().toggleDirectDropEnabled(playerId);
//                        player.sendMessage(ChatColor.GREEN + "§aDrop do ekwipunku został włączony.");
//                        break;
//                    } else if (line.contains("§cWyłącz Drop")) {
//                        plugin.getPlayerDropManager().toggleDirectDropEnabled(playerId);
//                        player.sendMessage(ChatColor.RED + "§cDrop do ekwipunku został wyłączony.");
//                        break;
//                    }
//                }
//            }
//        }
//
//        event.setCancelled(true);
//        plugin.getGUIManager().openDropSettingsGUI(player);
//    }

}
