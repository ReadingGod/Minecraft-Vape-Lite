package org.example;

import com.olziedev.playerwarps.api.PlayerWarpsAPI;
import com.olziedev.playerwarps.api.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class BuyTicket implements CommandExecutor {

    private final TicketCreator plugin;

    public BuyTicket(TicketCreator plugin) {
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
            player.sendMessage(ChatColor.RED + "Usage: /tickets buy <tier> <warp> <quantity?>");
            return true;
        } else if (args.length == 4) {
            quantity = Integer.parseInt(args[3]);
        } else {
            quantity = 1;
        }

        int tier = switch (args[1]) {
            default -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
        };

        String warpName = args[2];

        double playerBalance = TicketCreator.econ.getBalance(player);


        PlayerWarpsAPI.getInstance(api -> {
            boolean isOwnerOfWarp;

            if (Objects.equals(UsefulMethods.readConfig("owner-check"), "true")){
                Warp warp = api.getPlayerWarp(warpName, sender);

                UUID ownerUUID = warp.getUUID();

                UUID uuid = player.getUniqueId();

                isOwnerOfWarp = Objects.equals(uuid.toString(), ownerUUID.toString());

                if (!isOwnerOfWarp){
                    player.sendMessage(ColorUtil.translateHexColorCodes(UsefulMethods.getMessage("not-owner")));
                    return;
                }
            }

            double price = Double.parseDouble(UsefulMethods.readConfig("tiers." + tier + ".price")) * quantity;
            if (playerBalance < price){
                String message = UsefulMethods.getMessage("insufficient-balance");

                Map<String, String> map = new HashMap<>();
                map.put("price", Double.toString(price));
                map.put("balance", Double.toString(playerBalance));

                message = UsefulMethods.replacePlaceholders(message, map);

                player.sendMessage(ColorUtil.translateHexColorCodes(message));
                return;
            }

            TicketCreator.econ.withdrawPlayer(player, price);

            // Retrieve the item name and lore from the config
            String itemName = UsefulMethods.readConfig("tiers." + tier + ".name");
            List<String> itemLore = plugin.getConfig().getStringList("tiers." + tier + ".lore");

            // Replace %shopName% in the item name and lore
            itemName = itemName.replace("%warpName%", warpName);
           List<String> finalLore = new ArrayList<>();
            for (String line : itemLore) {
                finalLore.add(ColorUtil.translateHexColorCodes(line.replace("%warpName%", warpName)));
            }

            // Create the item
            ItemStack item = new ItemStack(Objects.requireNonNull(Material.getMaterial(UsefulMethods.readConfig("tiers." + tier + ".type")))); // Change the material as needed
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ColorUtil.translateHexColorCodes(itemName));
                meta.setLore(finalLore);

                if (Objects.equals(UsefulMethods.readConfig("tiers." + tier + ".glowing"), "true")){
                    meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }

                item.setItemMeta(meta);
            }

            item.setAmount(quantity);


            // Give the item to the player
            player.getInventory().addItem(item);

            String message = UsefulMethods.getMessage("successful-purchase");

            String tierIcon = switch (tier) {
                default -> "I";
                case 2 -> "II";
                case 3 -> "III";
                case 4 -> "IV";
                case 5 -> "V";
            };

            Map<String, String> map = new HashMap<>();
            map.put("tier", tierIcon);
            map.put("warpName", warpName);
            map.put("quantity", Integer.toString(quantity));
            map.put("price", Double.toString(price));

            message = UsefulMethods.replacePlaceholders(message, map);

            player.sendMessage(ColorUtil.translateHexColorCodes(message));
        });
        return true;
    }
}
