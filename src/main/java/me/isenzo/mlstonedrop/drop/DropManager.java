package me.isenzo.mlstonedrop.drop;

import me.isenzo.mlstonedrop.Main;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DropManager implements Listener {
    private final Main plugin;
    private final LuckPerms api;
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();

    public DropManager(Main plugin, LuckPerms api) {
        this.plugin = plugin;
        this.api = api;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // Blokuj drop z rud i na trybie kreatywnym
        if (player.getGameMode() == GameMode.CREATIVE || block.getType().name().endsWith("_ORE")) {
            event.setDropItems(false);
            return;
        }

        // Logika dla kamienia i trybu przetrwania
        if (block.getType() == Material.STONE && player.getGameMode() == GameMode.SURVIVAL) {
            boolean directDropEnabled = plugin.getPlayerDropManager().isDirectDropEnabled(player.getUniqueId());
            boolean isCobbleDropEnabled = plugin.getPlayerDropManager().isCobbleDropEnabled(player.getUniqueId());

            // Anuluj domyślny drop, aby samodzielnie kontrolować co wypada
            event.setDropItems(!isCobbleDropEnabled);

            // Losowanie dropu z zdefiniowanych przedmiotów
            Map<Material, Double> drops = plugin.getConfigManager().getDropChances();
            for (Material material : drops.keySet()) {
                double chance = getChanceForPlayerAndMaterial(player, material);
                if (Math.random() < chance) {
                    ItemStack itemStack = new ItemStack(material, 1);
                    dropItem(player, block, itemStack, directDropEnabled);
                    return; // Zatrzymaj po pierwszym sukcesie, aby z jednego bloku wypadał tylko jeden przedmiot
                }
            }

            // Drop cobblestone, jeśli nie wypadł żaden inny przedmiot i drop cobblestone jest włączony
            if (isCobbleDropEnabled) {
                ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
                dropItem(player, block, cobblestone, directDropEnabled);
            }
        }
    }


    private void dropItem(Player player, Block block, ItemStack itemStack, boolean directDropEnabled) {
        if (directDropEnabled) {
            HashMap<Integer, ItemStack> overflow = player.getInventory().addItem(itemStack);
            overflow.forEach((index, overflowItem) -> block.getWorld().dropItemNaturally(block.getLocation(), overflowItem));
        } else {
            block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
        }
    }

    public double getChanceForPlayerAndMaterial(Player player, Material material) {
        String group = api.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        ConfigurationSection materialSection = plugin.getConfig().getConfigurationSection("drops." + material.name());
        double chance = materialSection.getDouble(group + "-chance", materialSection.getDouble("default-chance"));
        return chance;
    }

    public boolean isCobbleDropEnabled(UUID playerId) {
        return cobbleDropEnabledForPlayer.getOrDefault(playerId, true);
    }

    public void toggleCobbleDropEnabled(UUID playerId) {
        cobbleDropEnabledForPlayer.put(playerId, !cobbleDropEnabledForPlayer.getOrDefault(playerId, true));
    }

}
