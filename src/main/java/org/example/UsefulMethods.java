package org.example;

import java.util.Map;

public class UsefulMethods {

    public static String readConfig(String path){
        return TicketCreator.config.getString(path);
    }

    public static String getMessage(String path){
        return readConfig("messages.prefix") + "&r " + readConfig("messages." + path);
    }

    public static String replacePlaceholders(String template, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("%" + entry.getKey() + "%", entry.getValue());
        }
        return template;
    }
}