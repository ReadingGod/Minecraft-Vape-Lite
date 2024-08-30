package org.example;

import com.olziedev.playerwarps.api.PlayerWarpsAPI;
import com.olziedev.playerwarps.api.player.WPlayer;
import com.olziedev.playerwarps.api.warp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class PluginTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (command.getName().equalsIgnoreCase("utc")) {
            if (args.length == 1) {
                if (sender.hasPermission("utc.reload")) {
                    suggestions.add("reload");
                }
                if (sender.hasPermission("utc.create")) {
                    suggestions.add("create");
                }
            } else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("utc.create")) {
                    // Suggest possible shop names here
                    Collection<String> playerNames = new ArrayList<>();
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        playerNames.add(player.getName());
                    }
                    suggestions.addAll(playerNames);
                }
            } else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("utc.create")) {
                    // Suggest possible shop names here
                    Collection<String> warpNames = new ArrayList<>();
                    PlayerWarpsAPI.getInstance(api -> {
                        Player player = Bukkit.getPlayer(args[1]);
                        if (player == null){
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                            WPlayer owner = api.getWarpPlayer(offlinePlayer.getUniqueId());
                            List<Warp> warps = owner.getWarps(true);
                            for (Warp warp : warps) {
                                warpNames.add(warp.getWarpName());
                            }
                        }
                        if (player != null){
                            WPlayer owner = api.getWarpPlayer(player.getUniqueId());
                            List<Warp> warps = owner.getWarps(true);
                            for (Warp warp : warps) {
                                warpNames.add(warp.getWarpName());
                            }
                        }
                    });
                    if (warpNames.isEmpty()) {
                        suggestions.add("No warps for this player!");
                    } else {
                        suggestions.addAll(warpNames);
                    }
                }
            } else if (args.length == 4 && args[0].equalsIgnoreCase("create")) {
                if (sender.hasPermission("utc.create")) {
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
