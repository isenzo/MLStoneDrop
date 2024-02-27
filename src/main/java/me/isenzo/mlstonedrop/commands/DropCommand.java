package me.isenzo.mlstonedrop.commands;

import me.isenzo.mlstonedrop.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DropCommand implements CommandExecutor {
    private Main plugin;

    public DropCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!(sender instanceof Player)) {
            sender.sendMessage("§8[§6MLDrop§8]§c Ta komenda może być użyta tylko przez gracza.");
            return true;
        }

        plugin.getGUIManager().openDropSettingsGUI(player);
        return true;
    }

}
