package org.example;

import com.olziedev.playerwarps.api.PlayerWarpsAPI;
import com.olziedev.playerwarps.api.player.WPlayer;
import com.olziedev.playerwarps.api.warp.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class PluginTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return null;
        }

        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("tickets")) {
            if (args.length == 1) {
                if (sender.hasPermission("tickets.reload")) {
                    suggestions.add("reload");
                }
                if (sender.hasPermission("tickets.buy")) {
                    suggestions.add("buy");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("buy")) {
                if (sender.hasPermission("tickets.buy")) {
                    suggestions.add("I");
                    suggestions.add("II");
                    suggestions.add("III");
                    suggestions.add("IV");
                    suggestions.add("V");
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("buy")) {
                if (sender.hasPermission("tickets.buy")) {
                    // Suggest possible shop names here
                    Collection<String> warpNames = new ArrayList<>();
                    PlayerWarpsAPI.getInstance(api -> {
                        WPlayer owner = api.getWarpPlayer(player.getUniqueId());
                        List<Warp> warps = owner.getWarps(true);
                        for (Warp warp : warps) {
                            warpNames.add(warp.getWarpName());
                        }
                    });
                    if (!warpNames.isEmpty()) {
                        suggestions.addAll(warpNames);
                    }
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("buy")) {
                if (sender.hasPermission("tickets.buy")) {
                    // Suggest possible quantities here
                    suggestions.add("1");
                    suggestions.add("32");
                    suggestions.add("64");
                }
            }
        }
        return suggestions;
    }
}
