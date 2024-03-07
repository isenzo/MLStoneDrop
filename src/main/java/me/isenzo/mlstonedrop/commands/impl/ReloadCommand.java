package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.Main;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class ReloadCommand implements CommandAction {

    private final Main plugin;

    public boolean handleReloadCommand(CommandSender sender) {
        Player player = (Player) sender;
        plugin.getConfigManager().reloadConfig();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§8[§6MLDrop§8]§c zaktualizowano plik konfiguracyjny."));
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleReloadCommand(sender);
        return true;
    }
}
