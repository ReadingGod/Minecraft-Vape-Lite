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
                    boolean tier1 = sender.hasPermission("tickets.tier.1");
                    boolean tier2 = sender.hasPermission("tickets.tier.2");
                    boolean tier3 = sender.hasPermission("tickets.tier.3");
                    boolean tier4 = sender.hasPermission("tickets.tier.4");
                    boolean tier5 = sender.hasPermission("tickets.tier.5");
                    if (tier1 || tier2 || tier3 || tier4 || tier5)
                        suggestions.add("I");
                    if (tier2 || tier3 || tier4 || tier5)
                        suggestions.add("II");
                    if (tier3 || tier4 || tier5)
                        suggestions.add("III");
                    if (tier4 || tier5)
                        suggestions.add("IV");
                    if (tier5)
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
