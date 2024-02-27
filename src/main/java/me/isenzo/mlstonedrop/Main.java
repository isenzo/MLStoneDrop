package me.isenzo.mlstonedrop;

import org.bukkit.plugin.java.JavaPlugin;

public final class MLStoneDrop extends JavaPlugin {
    private static Main instance;
    private ConfigManager configManager;
    private DropManager dropManager;
    private GUIManager guiManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        this.dropManager = new DropManager(this);
        this.guiManager = new GUIManager(this);

        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        getLogger().info("Plugin został włączony.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin został wyłączony.");
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public DropManager getDropManager() {
        return dropManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }
}
