package me.isenzo.mlstonedrop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.isenzo.mlstonedrop.commands.AdminCommand;
import me.isenzo.mlstonedrop.commands.DropCommand;
import me.isenzo.mlstonedrop.commands.impl.*;
import me.isenzo.mlstonedrop.commands.validation.HeightValidator;
import me.isenzo.mlstonedrop.config.ConfigManager;
import me.isenzo.mlstonedrop.drop.DropManager;
import me.isenzo.mlstonedrop.gui.GUIManager;
import me.isenzo.mlstonedrop.listeners.EventListener;
import me.isenzo.mlstonedrop.player.PlayerDropManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public final class Main extends JavaPlugin {
    private ConfigManager configManager;
    private DropManager dropManager;
    private GUIManager guiManager;
    private PlayerDropManager playerDropManager;
    private LuckPerms api;
    private BukkitTask task;
    private UpdateCommand updateCommand;
    private AddCommand addCommand;
    private DelCommand delCommand;
    private UpdateNameCommand updateNameCommand;
    private UpdateChanceCommand updateChanceCommand;
    private UpdateHeightCommand updateHeightCommand;
    private HelpCommand helpCommand;
    private ListCommand listCommand;
    private ReloadCommand reloadCommand;
    private HeightValidator heightValidator;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<LuckPerms> provider = getServer().getServicesManager().getRegistration(LuckPerms.class);
        if (Objects.nonNull(provider)) {
            api = provider.getProvider();
        }
        this.configManager = new ConfigManager(this);
        this.dropManager = new DropManager(this, api);
        this.guiManager = new GUIManager(this);
        this.playerDropManager = new PlayerDropManager(this);
        this.updateCommand = new UpdateCommand(this);
        this.addCommand = new AddCommand(this);
        this.delCommand = new DelCommand(this);
        this.updateNameCommand = new UpdateNameCommand(this);
        this.updateChanceCommand = new UpdateChanceCommand(this);
        this.updateHeightCommand = new UpdateHeightCommand(this);
        this.helpCommand = new HelpCommand();
        this.listCommand = new ListCommand(this);
        this.reloadCommand = new ReloadCommand(this);
        this.heightValidator = new HeightValidator(this);

        this.getCommand("mldrop").setExecutor(new AdminCommand(this));
        this.getCommand("drop").setExecutor(new DropCommand(this));


        getServer().getPluginManager().registerEvents(new EventListener(this), this);
    }

    @Override
    public void onDisable() {
        if (Objects.nonNull(task) && !task.isCancelled()) {
            task.cancel();
        }
    }
}