package me.isenzo.mlstonedrop.player;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerDropManager {

    private final Main plugin;

    private final HashMap<UUID, Boolean> cobbleDropFromStone = new HashMap<>();
    private final HashMap<UUID, Boolean> directDrop = new HashMap<>();
    private final Map<UUID, Integer> playerPages = new HashMap<>();

    public boolean directDrop(UUID playerId) {
        return directDrop.getOrDefault(playerId, true);
    }

    public boolean cobbleDropFromStone(UUID playerId) {
        return cobbleDropFromStone.getOrDefault(playerId, true);
    }

    public void toggleDirectDrop(UUID playerId) {
        directDrop.put(playerId, !directDrop(playerId));
        saveDropSettings();
    }

    public void toggleCobbleDropFromStone(UUID playerId) {
        cobbleDropFromStone.put(playerId, !cobbleDropFromStone(playerId));
        saveDropSettings();
    }

    public void saveDropSettings() {
        File file = new File(plugin.getDataFolder(), "playerDropSettings.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        cobbleDropFromStone.forEach((uuid, enabled) -> config.set(uuid.toString() + ".cobbleDrop", enabled));
        directDrop.forEach((uuid, enabled) -> config.set(uuid.toString() + ".directDrop", enabled));

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Material> getAllowedPickaxes(Material material) {
        String path = "drops." + material.name() + ".allowed-pickaxe";
        FileConfiguration config = plugin.getConfig();
        if (config.contains(path)) {
            return config.getStringList(path).stream()
                    .map(Material::matchMaterial)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Integer getPlayerPage(Player player) {
        return playerPages.getOrDefault(player.getUniqueId(), 1);
    }

    public void setPlayerPage(Player player, int page) {
        playerPages.put(player.getUniqueId(), page);
    }

}