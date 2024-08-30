package org.example;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {

    public static String translateHexColorCodes(String message) {
        final char COLOR_CHAR = '&';
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == COLOR_CHAR && i + 1 < message.length()) {
                if (message.charAt(i + 1) == '#') {
                    // Hex color code (e.g., &#FF5733)
                    String hexCode = message.substring(i + 1, i + 8);  // Gets #FF5733
                    builder.append(ChatColor.of(hexCode));
                    i += 7; // Skip over the hex code
                    continue;
                } else if ("0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(message.charAt(i + 1)) > -1) {
                    // Standard color code (e.g., &6)
                    builder.append(ChatColor.getByChar(message.charAt(i + 1)));
                    i++;
                    continue;
                }
            }
            builder.append(message.charAt(i));
        }
        return builder.toString();
    }
}
