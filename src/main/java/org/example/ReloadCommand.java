package org.example;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class ReloadCommand implements CommandExecutor {
    private final TicketCreator plugin;

    public ReloadCommand(TicketCreator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Checking the sender for required permission
        if (sender.hasPermission("utc.reload")) {

            // Getting the time before the reload
            long before = System.currentTimeMillis();

            // Reloading the plugin
            plugin.reload();

            // Getting the time after the reload
            long after = System.currentTimeMillis();

            // Sending message with the total time for the reload
            sender.sendMessage(ChatColor.GREEN + "Successfully reloaded TicketCreator in " + ChatColor.YELLOW + (after - before) + ChatColor.GREEN + " ms!");
        }
        return true;
    }
}