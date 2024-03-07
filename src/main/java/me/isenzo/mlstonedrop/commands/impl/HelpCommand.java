package me.isenzo.mlstonedrop.commands.impl;

import lombok.RequiredArgsConstructor;
import me.isenzo.mlstonedrop.commands.actions.CommandAction;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class HelpCommand implements CommandAction {

    public boolean handleHelpCommand(CommandSender sender) {
        sender.sendMessage("§9==============================================================");
        sender.sendMessage("§8[§6MLDrop§8] §6Lista dostępnych komend §cAdmina: ");
        sender.sendMessage("§e» §7/mldrop admin update §f<materiał> <ranga> <szansa> <nazwa>");
        sender.sendMessage("§e» §7/mldrop admin update-chance §f<materiał> <ranga> <szansa>");
        sender.sendMessage("§e» §7/mldrop admin update-name §f<materiał> <nazwa>");
        sender.sendMessage("§e» §7/mldrop admin drop-height §f<materiał> §a<min. wysokość §a(y)> §c<max wysokość §c(y)>");
        sender.sendMessage("§e» §7/mldrop admin add §f<materiał>");
        sender.sendMessage("§e» §7/mldrop admin delete lub del §f<materiał>");
        sender.sendMessage("§e» §7/mldrop admin list lub ls");
        sender.sendMessage("§e» §7/mldrop admin reload lub rl");
        sender.sendMessage("§9==============================================================");

        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        handleHelpCommand(sender);
        return true;
    }
}
