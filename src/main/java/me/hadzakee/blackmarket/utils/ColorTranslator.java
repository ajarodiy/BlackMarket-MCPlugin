package me.hadzakee.blackmarket.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

public class ColorTranslator {
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    public ColorTranslator() {
    }

    public static String translateColorCodes(String text) {
        String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        StringBuilder finalText = new StringBuilder();

        for(int i = 0; i < texts.length; ++i) {
            if (texts[i].equalsIgnoreCase("&")) {
                ++i;
                if (texts[i].charAt(0) == '#') {
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                } else {
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            } else {
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static TextComponent translateColorCodesToTextComponent(String text) {
        String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        ComponentBuilder builder = new ComponentBuilder();

        for(int i = 0; i < texts.length; ++i) {
            TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")) {
                ++i;
                if (texts[i].charAt(0) == '#') {
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(ChatColor.of(texts[i].substring(0, 7)));
                    builder.append(subComponent);
                } else {
                    if (texts[i].length() > 1) {
                        subComponent.setText(texts[i].substring(1));
                    } else {
                        subComponent.setText(" ");
                    }

                    switch(texts[i].charAt(0)) {
                        case '0':
                            subComponent.setColor(ChatColor.BLACK);
                            break;
                        case '1':
                            subComponent.setColor(ChatColor.DARK_BLUE);
                            break;
                        case '2':
                            subComponent.setColor(ChatColor.DARK_GREEN);
                            break;
                        case '3':
                            subComponent.setColor(ChatColor.DARK_AQUA);
                            break;
                        case '4':
                            subComponent.setColor(ChatColor.DARK_RED);
                            break;
                        case '5':
                            subComponent.setColor(ChatColor.DARK_PURPLE);
                            break;
                        case '6':
                            subComponent.setColor(ChatColor.GOLD);
                            break;
                        case '7':
                            subComponent.setColor(ChatColor.GRAY);
                            break;
                        case '8':
                            subComponent.setColor(ChatColor.DARK_GRAY);
                            break;
                        case '9':
                            subComponent.setColor(ChatColor.BLUE);
                        case ':':
                        case ';':
                        case '<':
                        case '=':
                        case '>':
                        case '?':
                        case '@':
                        case 'A':
                        case 'B':
                        case 'C':
                        case 'D':
                        case 'E':
                        case 'F':
                        case 'G':
                        case 'H':
                        case 'I':
                        case 'J':
                        case 'K':
                        case 'L':
                        case 'M':
                        case 'N':
                        case 'O':
                        case 'P':
                        case 'Q':
                        case 'R':
                        case 'S':
                        case 'T':
                        case 'U':
                        case 'V':
                        case 'W':
                        case 'X':
                        case 'Y':
                        case 'Z':
                        case '[':
                        case '\\':
                        case ']':
                        case '^':
                        case '_':
                        case '`':
                        case 'g':
                        case 'h':
                        case 'i':
                        case 'j':
                        case 'p':
                        case 'q':
                        default:
                            break;
                        case 'a':
                            subComponent.setColor(ChatColor.GREEN);
                            break;
                        case 'b':
                            subComponent.setColor(ChatColor.AQUA);
                            break;
                        case 'c':
                            subComponent.setColor(ChatColor.RED);
                            break;
                        case 'd':
                            subComponent.setColor(ChatColor.LIGHT_PURPLE);
                            break;
                        case 'e':
                            subComponent.setColor(ChatColor.YELLOW);
                            break;
                        case 'f':
                            subComponent.setColor(ChatColor.WHITE);
                            break;
                        case 'k':
                            subComponent.setObfuscated(true);
                            break;
                        case 'l':
                            subComponent.setBold(true);
                            break;
                        case 'm':
                            subComponent.setStrikethrough(true);
                            break;
                        case 'n':
                            subComponent.setUnderlined(true);
                            break;
                        case 'o':
                            subComponent.setItalic(true);
                            break;
                        case 'r':
                            subComponent.setColor(ChatColor.RESET);
                    }

                    builder.append(subComponent);
                }
            } else {
                builder.append(texts[i]);
            }
        }

        return new TextComponent(builder.create());
    }
}
