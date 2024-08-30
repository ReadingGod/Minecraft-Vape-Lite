package org.example;

import com.olziedev.playerwarps.api.PlayerWarpsAPI;
import com.olziedev.playerwarps.api.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateTicket implements CommandExecutor {

    private final TicketCreator plugin;

    public CreateTicket(TicketCreator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        int quantity;

        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: /utc create <player> <warp> <quantity?>");
            return true;
        } else if (args.length == 4) {
            quantity = Integer.parseInt(args[3]);
        } else {
            quantity = 1;
        }

        String playerName = args[1];
        String warpName = args[2];



        PlayerWarpsAPI.getInstance(api -> {
            boolean isOwnerOfWarp;

            if (UsefulMethods.readConfig("owner-check") == "false"){
                isOwnerOfWarp = true;
            } else {
                Warp warp = api.getPlayerWarp(warpName, sender);

                UUID ownerUUID = warp.getUUID();

                Player owner = Bukkit.getPlayer(playerName);

                UUID uuid;

                if (owner == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
                    uuid = offlinePlayer.getUniqueId();
                } else {
                    uuid = owner.getUniqueId();

                }

                isOwnerOfWarp = Objects.equals(uuid.toString(), ownerUUID.toString());

                if (!isOwnerOfWarp){
                    player.sendMessage(ChatColor.RED + "The user is not owner of this warp!");
                    return;
                }
            }



        // Retrieve the item name and lore from the config
        String itemName = UsefulMethods.readConfig("item-name");
        List<String> itemLore = plugin.getConfig().getStringList("item-lore");

        // Replace %shopName% in the item name and lore
        itemName = itemName.replace("%shopName%", warpName);
        List<String> finalLore = new ArrayList<>();
        for (String line : itemLore) {
            finalLore.add(ColorUtil.translateHexColorCodes(line.replace("%shopName%", warpName)));
        }

        // Create the item
        ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(UsefulMethods.readConfig("item-type")))); // Change the material as needed
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ColorUtil.translateHexColorCodes(itemName));
            meta.setLore(finalLore);

            if (Objects.equals(UsefulMethods.readConfig("glowing"), "true")){
                meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(meta);
        }

        item.setAmount(quantity);


        // Give the item to the player
        player.getInventory().addItem(item);
        player.sendMessage(ChatColor.GREEN + "You have received a ticket for player warp " + ChatColor.YELLOW + warpName + ChatColor.GREEN + "!");
        });
        return true;
    }
}
