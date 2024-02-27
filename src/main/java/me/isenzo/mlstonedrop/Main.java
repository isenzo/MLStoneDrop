package me.isenzo.mlstonedrop;

import me.isenzo.mlstonedrop.commands.DropCommand;
import me.isenzo.mlstonedrop.commands.UpdateValueCommand;
import me.isenzo.mlstonedrop.config.ConfigManager;
import me.isenzo.mlstonedrop.drop.DropManager;
import me.isenzo.mlstonedrop.gui.GUIManager;
import me.isenzo.mlstonedrop.listeners.EventListener;
import me.isenzo.mlstonedrop.player.PlayerDropManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin{
    private static Main instance;
    private ConfigManager configManager;
    private DropManager dropManager;
    private GUIManager guiManager;
    private PlayerDropManager playerDropManager;
    private LuckPerms api;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            api = provider.getProvider();
        }
        instance = this;

        this.configManager = new ConfigManager(this);
        this.dropManager = new DropManager(this, api);
        this.guiManager = new GUIManager(this);
        this.playerDropManager = new PlayerDropManager(this);

        this.getCommand("mldrop").setExecutor(new UpdateValueCommand(this));
        this.getCommand("drop").setExecutor(new DropCommand(this));

        getServer().getPluginManager().registerEvents(new EventListener(this), this);
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

    public PlayerDropManager getPlayerDropManager() {
        return playerDropManager;
    }
}
