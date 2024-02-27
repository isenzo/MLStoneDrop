package me.isenzo.mlstonedrop.player;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDropManager {
    private Main plugin;
    public PlayerDropManager(Main plugin) {
        this.plugin = plugin;
    }
    private final HashMap<UUID, Boolean> cobbleDropEnabledForPlayer = new HashMap<>();
    private final HashMap<UUID, Boolean> directDropEnabledForPlayer = new HashMap<>();

    public boolean isDirectDropEnabled(UUID playerId) {
        return directDropEnabledForPlayer.getOrDefault(playerId, true); // Domyślnie włączony
    }

    public void toggleDirectDropEnabled(UUID playerId) {
        directDropEnabledForPlayer.put(playerId, !isDirectDropEnabled(playerId));
        saveDropSettings(); // Zapisz zmiany od razu po ich wykonaniu
    }

    public boolean isCobbleDropEnabled(UUID playerId) {
        return cobbleDropEnabledForPlayer.getOrDefault(playerId, true); // Domyślnie włączony
    }

    public void toggleCobbleDropEnabled(UUID playerId) {
        cobbleDropEnabledForPlayer.put(playerId, !isCobbleDropEnabled(playerId));
        saveDropSettings();
    }

    // Zapisz ustawienia dropu do pliku
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

    // Wczytaj ustawienia dropu z pliku
    public void loadDropSettings() {
        File file = new File(plugin.getDataFolder(), "playerDropSettings.yml");
        if (!file.exists()) return;
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.getKeys(false).forEach(key -> {
            UUID uuid = UUID.fromString(key);
            cobbleDropEnabledForPlayer.put(uuid, config.getBoolean(key + ".cobbleDropEnabled", true));
            directDropEnabledForPlayer.put(uuid, config.getBoolean(key + ".directDropEnabled", true));
        });
    }
}
