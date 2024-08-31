package org.example;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class TicketCreator extends JavaPlugin {
    public static FileConfiguration config;
    public static Economy econ;
    @Override
    public void onEnable() {
        setupEconomy();

        if (getServer().getPluginManager().getPlugin("PlayerWarps") == null) {
            // Dependency not found, log a warning and disable the plugin
            getLogger().severe("Dependency 'PlayerWarps' not found! Disabling UltrapixelTicketCreator...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("'PlayerWarps' dependency found!");
        // Copy the config.yml in the plugin configuration folder if it doesn't exist.
        saveDefaultConfig();

        config = this.getConfig();

        boolean enable = getConfig().getBoolean("enable");

        if (enable) {
            // Registering the commands
            registerCommands();

            // Logging the successful enabling of the plugin
            getLogger().info("UltrapixelTicketCreator is enabled!");
        } else {
            getLogger().severe("UltrapixelTicketCreator is not enabled in the config!");
        }
    }

    private void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault not found! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            getLogger().severe("No economy plugin found! Plugin disabled.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        econ = rsp.getProvider();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tickets")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /tickets <subcommand>");
                return true;
            }

            String subCommand = args[0].toLowerCase();

            if (subCommand.equals("reload")) {
                ReloadCommand reload = new ReloadCommand(this);
                reload.onCommand(sender, command, label, args);
            } else if (subCommand.equals("buy")) {
                BuyTicket ticket = new BuyTicket(this);
                ticket.onCommand(sender, command, label, args);
            } else {
                sender.sendMessage("Unknown subcommand.");
            }
            return true;
        }

        return false;
    }

    // Reloads the plugin
    public void reload() {
        // Reloads the configuration
        reloadConfig();

        config = this.getConfig();
    }

    private void registerCommands() {
        // Registers the reload command
        Objects.requireNonNull(this.getCommand("tickets")).setExecutor(this);
        Objects.requireNonNull(this.getCommand("tickets")).setTabCompleter(new PluginTabCompleter());
    }

}