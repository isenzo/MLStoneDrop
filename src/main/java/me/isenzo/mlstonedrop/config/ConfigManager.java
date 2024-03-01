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
                        double chance = materialSection.getDouble("default-chance", 0.0);
                        dropChances.put(material, chance);
                    }
                }
            }
        }
        return dropChances;
    }

    public int[] getDropHeightRange(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection dropSection = getConfig().getConfigurationSection("drops." + key);
        if (dropSection != null) {
            int minY = dropSection.getInt("min-y", 0);
            int maxY = dropSection.getInt("max-y", 256);
            return new int[]{minY, maxY};
        }
        return new int[]{0, 256};
    }

    public double getGroupDefaultChance(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble("default-chance", 0.0); // Domyślna szansa dropu
        }
        return 0.0;
    }

    public double getGroupVipChance(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble("vip-chance", 0.0);
        }
        return 0.0;
    }

    public double getGroupSvipChance(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble("svip-chance", 0.0);
        }
        return 0.0;
    }

    public double getGroupSponsorChance(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getDouble("sponsor-chance", 0.0);
        }
        return 0.0;
    }

    public String getItemName(Material material) {
        String key = material.name().toLowerCase();
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + key);
        if (materialSection != null) {
            return materialSection.getString("item_name", material.name());
        }
        return material.name();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }

}