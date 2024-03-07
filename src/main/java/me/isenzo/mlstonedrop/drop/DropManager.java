package me.isenzo.mlstonedrop.drop;

import lombok.Getter;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.config.DropChance;
import net.luckperms.api.LuckPerms;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class DropManager implements Listener {
    private final Main plugin;
    private final LuckPerms api;
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();
    private final String PLAYER_NOT_FOUND = "[MLDrop-LuckPerms] Nie znaleziono gracza!";

    public DropManager(Main plugin, LuckPerms api) {
        this.plugin = plugin;
        this.api = api;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        boolean cobbleDropFromStone = plugin.getPlayerDropManager().cobbleDropFromStone(player.getUniqueId());
        boolean directDrop = plugin.getPlayerDropManager().directDrop(player.getUniqueId());

        if (player.getGameMode() == GameMode.CREATIVE) {
            event.setDropItems(false);
            return;
        }

        if (block.getType().name().endsWith("_ORE") && player.getGameMode() == GameMode.SURVIVAL && !block.getType().name().equals("NETHER_GOLD_ORE")) {
            dropCobbleStoneFromOres(player, cobbleDropFromStone, block, directDrop, event);
            event.setExpToDrop(0);
        }

        if (block.getType().name().equals("NETHER_GOLD_ORE") && player.getGameMode() == GameMode.SURVIVAL) {
            dropNetherrackFromNetherGoldOre(block, event);
            event.setExpToDrop(0);
        }

        if (block.getType() == Material.STONE && player.getGameMode() == GameMode.SURVIVAL) {
            int playerY = block.getLocation().getBlockY();

            if (!cobbleDropFromStone) {
                event.setDropItems(false);
            }

            Map<Material, DropChance> dropChances = plugin.getConfigManager().getDropChances();
            Optional<Material> randomDrop = dropChances.entrySet().stream()
                    .filter(entry -> playerY >= entry.getValue().getMinY() && playerY <= entry.getValue().getMaxY())
                    .filter(entry -> {
                        double chance = getChanceForPlayerAndMaterial(player, entry.getKey());
                        return Math.random() < chance;
                    })
                    .map(Map.Entry::getKey)
                    .findAny();

            dropItemToInventoryAndDropCobbleStone(randomDrop, player, directDrop, block, event, cobbleDropFromStone);
        }
    }

    private void dropItemToInventoryAndDropCobbleStone(Optional<Material> randomDrop, Player player, Boolean directDrop, Block block, BlockBreakEvent event, Boolean cobbleDropFromStone) {
        if (randomDrop.isPresent()) {
            event.setDropItems(false);
            ItemStack itemStack = new ItemStack(randomDrop.get(), 1);
            dropItem(player, block, itemStack, directDrop);
        } else if (cobbleDropFromStone) {
            ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
            if (directDrop) {
                HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(cobblestone);
                if (!notAddedItems.isEmpty()) {
                    notAddedItems.values().forEach(overflowItem ->
                            block.getWorld().dropItemNaturally(block.getLocation(), overflowItem)
                    );
                }
            } else {
                block.getWorld().dropItemNaturally(block.getLocation(), cobblestone);
            }
            event.setDropItems(false);
        }
    }

    private void dropItem(Player player, Block block, ItemStack itemStack, boolean directDrop) {
        if (directDrop) {
            HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(itemStack);
            if (!notAddedItems.isEmpty()) {
                notAddedItems.values().forEach(overflowItem ->
                        block.getWorld().dropItemNaturally(block.getLocation(), overflowItem)
                );
            }
        } else {
            block.getWorld().dropItemNaturally(block.getLocation(), itemStack);
        }
    }

    private void dropCobbleStoneFromOres(Player player, Boolean cobbleDropFromStone, Block block, Boolean directDrop, BlockBreakEvent event) {
        if (cobbleDropFromStone) {
            ItemStack cobblestone = new ItemStack(Material.COBBLESTONE, 1);
            if (directDrop) {
                HashMap<Integer, ItemStack> notAddedItems = player.getInventory().addItem(cobblestone);
                if (!notAddedItems.isEmpty()) {
                    notAddedItems.values().forEach(overflowItem ->
                            block.getWorld().dropItemNaturally(block.getLocation(), overflowItem)
                    );
                }
            } else {
                block.getWorld().dropItemNaturally(block.getLocation(), cobblestone);
            }
            event.setDropItems(false);
        }
    }

    private void dropNetherrackFromNetherGoldOre(Block block, BlockBreakEvent event) {
        ItemStack netherrack = new ItemStack(Material.NETHERRACK, 1);
        block.getWorld().dropItemNaturally(block.getLocation(), netherrack);
        event.setDropItems(false);
    }

    public double getChanceForPlayerAndMaterial(Player player, Material material) {
        String group = api.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();
        DropChance dropChance = plugin.getConfigManager().getDropChances().get(material);

        if (Objects.nonNull(dropChance) && Objects.nonNull(group)) {
            switch (group.toLowerCase()) {
                case "vip":
                    return dropChance.getVipChance();
                case "svip":
                    return dropChance.getSvipChance();
                case "sponsor":
                    return dropChance.getSponsorChance();
                default:
                    return dropChance.getDefaultChance();
            }
        }
        return plugin.getConfigManager().getGroupDefaultChance(material);
    }
}