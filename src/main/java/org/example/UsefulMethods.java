package org.example;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UsefulMethods {

    public static String readConfig(String path){
        return TicketCreator.config.getString(path);
    }

    public static String getMessage(String path){
        return readConfig("messages.prefix") + "&r " + readConfig("messages." + path);
    }
    public static boolean hasEnoughSpace(Player player, ItemStack item) {
        int amountNeeded = item.getAmount();

        // Iterate only through the main inventory slots (0-35)
        for (int i = 0; i < 36; i++) { // 0-35 are the main inventory slots
            ItemStack invItem = player.getInventory().getItem(i);

            // If the slot is empty, consider the full slot space as available
            if (invItem == null || invItem.getType() == org.bukkit.Material.AIR) {
                amountNeeded -= item.getMaxStackSize();
            }
            // If the slot contains the same type of item with the same meta, calculate how much space is left in that stack
            else if (invItem.isSimilar(item) && Objects.equals(invItem.getItemMeta(), item.getItemMeta())) {
                amountNeeded -= (invItem.getMaxStackSize() - invItem.getAmount());
            }

            // If the amount needed is zero or less, the player has enough space
            if (amountNeeded <= 0) {
                return true;
            }
        }

        // If we exit the loop and still need more space, return false
        return false;
    }

    public static void sendMessage(Player player, Map<String, String> map, String path){
        String message = UsefulMethods.getMessage(path);

        message = UsefulMethods.replacePlaceholders(message, map);

        player.sendMessage(ColorUtil.translateHexColorCodes(message));
    }

    public static String replacePlaceholders(String template, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return template;
    }
}