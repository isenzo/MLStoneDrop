package me.isenzo.mlstonedrop.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public Map<Material, Double> getDropChances() {
        Map<Material, Double> dropChances = new HashMap<>();
        ConfigurationSection dropsSection = getConfig().getConfigurationSection("drops");
        if (dropsSection != null) {
            for (String key : dropsSection.getKeys(false)) {
                Material material = Material.matchMaterial(key);
                if (material != null) {
                    ConfigurationSection materialSection = dropsSection.getConfigurationSection(key);
                    if (materialSection != null) {
                        // Now we consider the highest priority chance available for the player
                        double chance = materialSection.getDouble("default-chance", 0.0);
                        dropChances.put(material, chance);
                    }
                }
            }
        }
        return dropChances;
    }

    // Nowa metoda do pobierania zakresu wysokości, z której przedmiot może być zrzucany.
    public int[] getDropHeightRange(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection dropSection = getConfig().getConfigurationSection("drops." + key);
        if (dropSection != null) {
            int minY = dropSection.getInt("min-y", 0); // Domyślna minimalna wysokość
            int maxY = dropSection.getInt("max-y", 256); // Domyślna maksymalna wysokość
            return new int[]{minY, maxY};
        }
        return new int[]{0, 256}; // Zakres domyślny, jeśli nie określono
    }

    // Metody do pobierania szansy dropu dla różnych grup uprzywilejowanych
    public double getGroupChance(Material material, String group) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble(group + "-chance", getGroupDefaultChance(material)); // Pobiera szansę dla grupy lub domyślną szansę
        }
        return 0.0;
    }

    // Metoda do pobierania domyślnej szansy dropu dla danego materiału
    public double getGroupDefaultChance(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble("default-chance", 0.0); // Domyślna szansa dropu
        }
        return 0.0;
    }

    public String getItemName(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getString("item_name", material.name()); // Domyślnie zwraca nazwę materialu, jeśli nie określono
        }
        return material.name(); // Zwraca nazwę materialu, jeśli sekcja nie istnieje
    }

    public void saveConfig() {
        plugin.saveConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

}