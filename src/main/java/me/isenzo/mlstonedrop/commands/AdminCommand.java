package me.isenzo.mlstonedrop.commands;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import me.isenzo.mlstonedrop.commands.impl.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
public class AdminCommand implements CommandExecutor {

    private final Main plugin;
    private Map<String, CommandAction> commands = new HashMap<>();
    public AdminCommand(Main plugin) {
        this.plugin = plugin;
        commands.put("update", new UpdateCommand(plugin));
        commands.put("update-name", new UpdateNameCommand(plugin));
        commands.put("update-chance", new UpdateChanceCommand(plugin));
        commands.put("drop-height", new UpdateHeightCommand(plugin));
        commands.put("add", new AddCommand(plugin));
        commands.put("del", new DelCommand(plugin));
        commands.put("help", new HelpCommand());
        commands.put("list", new ListCommand(plugin));
        commands.put("ls", new ListCommand(plugin));
        commands.put("reload", new ReloadCommand(plugin));
        commands.put("rl", new ReloadCommand(plugin));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("mldrop.admin")) {
            sender.sendMessage("§8[§6MLDrop§8] §cNie masz uprawnień do użycia tej komendy.");
            return true;
        }

        if (args.length < 2 || !args[0].equalsIgnoreCase("admin")) {
            plugin.getHelpCommand().handleHelpCommand(sender);
            return true;
        }

        if (args[1].equalsIgnoreCase("list") || args[1].equalsIgnoreCase("ls")) {
            return plugin.getListCommand().handleListCommand(sender);
        }

        if (args[1].equalsIgnoreCase("reload") || args[1].equalsIgnoreCase("rl")) {
            return plugin.getReloadCommand().handleReloadCommand(sender);
        }

        if (args[1].equalsIgnoreCase("drop-height") || args[1].equalsIgnoreCase("drop-h")) {
            return plugin.getUpdateHeightCommand().handleUpdateHeightCommand(sender, args);
        }

//        plugin.getHelpCommand().handleHelpCommand(sender);
//        return true;
        //TODO: Zacząłem wprowadzać interfejs, gdyby nie działało, to trzeba usunąć to poniżej, odkomentować to powyżej, i wywalić interfejs!
        CommandAction action = commands.get(args[1].toLowerCase());
        if (action != null) {
            return action.execute(sender, args);
        } else {
            plugin.getHelpCommand().handleHelpCommand(sender);
            return true;
        }
    }

}