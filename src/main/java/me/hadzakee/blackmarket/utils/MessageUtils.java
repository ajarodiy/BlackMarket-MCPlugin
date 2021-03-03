package me.hadzakee.blackmarket.utils;

public class MessageUtils {

    private static String prefix;
    private static String messagecolor;

    public static String message(String message) {
        return ColorTranslator.translateColorCodes(prefix + " " + messagecolor + message);
    }

    public static void setPrefix(String prefix) {
        MessageUtils.prefix = prefix;
    }

    public static void setMessagecolor(String color) {
        MessageUtils.messagecolor = color;
    }
}
