package me.isenzo.mlstonedrop.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ConfigManager {
    private final JavaPlugin plugin;
    private final String MATERIAL_NAME_NOT_FOUND = "§cBRAK NAZWY";

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }


    public Map<Material, DropChance> getDropChances() {
        Map<Material, DropChance> dropChances = new HashMap<>();
        ConfigurationSection dropsSection = getConfig().getConfigurationSection("drops");
        if (dropsSection != null) {
            for (String key : dropsSection.getKeys(false)) {
                Material material = Material.matchMaterial(key);
                if (material != null) {
                    ConfigurationSection materialSection = dropsSection.getConfigurationSection(key);
                    if (materialSection != null) {
                        DropChance dropChance = new DropChance();
                        dropChance.setDefaultChance(materialSection.getDouble("default-chance", 0.0));
                        dropChance.setVipChance(materialSection.getDouble("vip-chance", 0.0));
                        dropChance.setSvipChance(materialSection.getDouble("svip-chance", 0.0));
                        dropChance.setSponsorChance(materialSection.getDouble("sponsor-chance", 0.0));
                        dropChance.setMinY(materialSection.getInt("min-y", 0));
                        dropChance.setMaxY(materialSection.getInt("max-y", 256));
                        dropChances.put(material, dropChance);
                    }
                }
            }
        }
        return dropChances;
    }

    public int[] getDropHeightRange(Material material) {
        ConfigurationSection dropSection = getConfig().getConfigurationSection("drops." + material.name());
        if (dropSection != null) {
            int minY = dropSection.getInt("min-y", 0);
            int maxY = dropSection.getInt("max-y", 256);
            return new int[]{minY, maxY};
        }
        return new int[]{0, 256};
    }

    public int getMinY(Material material){
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getInt("min-y") : 0;
    }

    public int getMaxY(Material material){
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getInt("max-y") : 256;
    }

    public double getGroupDefaultChance(Material material) {
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getDouble("default-chance") : 0.0;
    }

    public double getGroupVipChance(Material material) {
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getDouble("vip-chance") : 0.0;
    }

    public double getGroupSvipChance(Material material) {
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getDouble("svip-chance") : 0.0;
    }

    public double getGroupSponsorChance(Material material) {
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        return Objects.nonNull(materialSection) ? materialSection.getDouble("sponsor-chance") : 0.0;
    }

    public String getItemName(Material material) {
        ConfigurationSection materialSection = getConfig().getConfigurationSection("drops." + material.name());
        String itemNameWithoutColor = Objects.nonNull(materialSection) ? materialSection.getString("item_name") : MATERIAL_NAME_NOT_FOUND;
        return Objects.nonNull(itemNameWithoutColor) ? ChatColor.translateAlternateColorCodes('&', itemNameWithoutColor) : MATERIAL_NAME_NOT_FOUND;
    }

    public void reloadConfig() {
        plugin.reloadConfig();
    }
}