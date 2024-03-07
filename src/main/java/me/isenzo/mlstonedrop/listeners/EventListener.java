package me.isenzo.mlstonedrop.listeners;

import lombok.AllArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

@AllArgsConstructor
public class EventListener implements Listener {
    private final Main plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();


        if (event.getView().getTitle().startsWith("§aDrop z kamienia")) {


            event.setCancelled(true);
            if (Objects.isNull(clickedItem) || !clickedItem.hasItemMeta()) return;

            ItemMeta meta = clickedItem.getItemMeta();
            if (Objects.isNull(meta)) return;

            if (meta.getDisplayName().equals("§eDrop cobblestone")) {
                plugin.getPlayerDropManager().toggleCobbleDropFromStone(player.getUniqueId());
                playSoundOnDirectDropOrCobbleDropChange(plugin.getPlayerDropManager().cobbleDropFromStone(player.getUniqueId()), player);
            } else if (meta.getDisplayName().equals("§eDrop do ekwipunku")) {
                plugin.getPlayerDropManager().toggleDirectDrop(player.getUniqueId());
                playSoundOnDirectDropOrCobbleDropChange(plugin.getPlayerDropManager().directDrop(player.getUniqueId()), player);
            }

            int currentPage = plugin.getPlayerDropManager().getPlayerPage(player);

            if ("§cPoprzednia strona".equals(meta.getDisplayName())) {
                currentPage = Math.max(1, currentPage - 1);
                plugin.getPlayerDropManager().setPlayerPage(player, currentPage);
                pageSound(player);
            } else if ("§eNastępna strona".equals(meta.getDisplayName())) {
                int totalPages = 10;
                currentPage = Math.min(totalPages, currentPage + 1);
                plugin.getPlayerDropManager().setPlayerPage(player, currentPage);
                pageSound(player);
            }

            plugin.getGuiManager().openDropSettingsGUI(player, currentPage);
        }
    }

    private void playSoundOnDirectDropOrCobbleDropChange(boolean isEnabled, Player player) {
        if (isEnabled) {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_ON, 1f, 2f);
        } else {
            player.playSound(player.getLocation(), Sound.BLOCK_STONE_BUTTON_CLICK_OFF, 1f, 2f);
        }
    }
    
    private void pageSound(Player player){
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 2f);
    }

}