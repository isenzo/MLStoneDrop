package me.isenzo.mlstonedrop.listeners;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener {
    private final Main plugin;


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();


        if (event.getView().getTitle().startsWith("§aDrop z kamienia")) {
            event.setCancelled(true);

            if (clickedItem == null || !clickedItem.hasItemMeta()) return;
            ItemMeta meta = clickedItem.getItemMeta();
            String displayName = meta.getDisplayName();
            if (meta == null) return;

            if (displayName.contains("Drop cobblestone")) {
                plugin.getPlayerDropManager().toggleCobbleDropEnabled(player.getUniqueId());
                playSoundOnChange(plugin.getPlayerDropManager().isCobbleDropEnabled(player.getUniqueId()), player);
            } else if (displayName.contains("Drop do ekwipunku")) {
                plugin.getPlayerDropManager().toggleDirectDropEnabled(player.getUniqueId());
                playSoundOnChange(plugin.getPlayerDropManager().isDirectDropEnabled(player.getUniqueId()), player);
            }

            plugin.getGUIManager().openDropSettingsGUI(player);
        }
    }

    private void playSoundOnChange(boolean isEnabled, Player player) {
        if (isEnabled) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f); // Wyższa tonacja
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1f, 2f); // Niższa tonacja
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1f, 2f);
        }
    }

    public EventListener(Main plugin) {
        this.plugin = plugin;
    }
}
