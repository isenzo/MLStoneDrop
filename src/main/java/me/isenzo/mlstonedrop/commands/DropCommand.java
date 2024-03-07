package me.isenzo.mlstonedrop.commands;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class DropCommand implements CommandExecutor {
    private final Main plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.getGuiManager().openDropSettingsGUI(((Player) sender).getPlayer(), 1);
            return true;
        } else {
            sender.sendMessage("§8[§6MLDrop§8]§c Ta komenda moze byc uzyta tylko przez gracza.");
            return true;
        }
    }
}