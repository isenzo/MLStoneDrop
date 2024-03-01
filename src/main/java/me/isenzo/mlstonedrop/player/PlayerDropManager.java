package me.isenzo.mlstonedrop.player;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDropManager {
    private Main plugin;
    public PlayerDropManager(Main plugin) {
        this.plugin = plugin;
    }
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();
    private final HashMap<UUID, Boolean> directDropEnabledForPlayer = new HashMap<>();

    public boolean isDirectDropEnabled(UUID playerId) {
        return directDropEnabledForPlayer.getOrDefault(playerId, true);
    }

    public boolean isCobbleDropEnabled(UUID playerId) {
        return cobbleDropEnabledForPlayer.getOrDefault(playerId, true);
    }

    public void toggleDirectDropEnabled(UUID playerId) {
        directDropEnabledForPlayer.put(playerId, !isDirectDropEnabled(playerId));
        saveDropSettings();
    }

    public void toggleCobbleDropEnabled(UUID playerId) {
        cobbleDropEnabledForPlayer.put(playerId, !isCobbleDropEnabled(playerId));
        saveDropSettings();
    }

    public void saveDropSettings() {
        File file = new File(plugin.getDataFolder(), "playerDropSettings.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        cobbleDropEnabledForPlayer.forEach((uuid, enabled) -> config.set(uuid.toString() + ".cobbleDropEnabled", enabled));
        directDropEnabledForPlayer.forEach((uuid, enabled) -> config.set(uuid.toString() + ".directDropEnabled", enabled));

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
