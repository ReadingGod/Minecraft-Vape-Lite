package org.example;

public class UsefulMethods {

    public static String readConfig(String path){
        return TicketCreator.config.getString(path);
    }
}