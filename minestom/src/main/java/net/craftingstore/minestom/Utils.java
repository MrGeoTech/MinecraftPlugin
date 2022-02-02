package net.craftingstore.minestom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Utils {

    public static Component color(String text) {
        text = text.replaceAll("§0", "<black>")
                .replaceAll("&1", "<dark_blue>")
                .replaceAll("&2", "<dark_green>")
                .replaceAll("&3", "<dark_aqua>")
                .replaceAll("&4", "<dark_red>")
                .replaceAll("&5", "<dark_purple>")
                .replaceAll("&6", "<gold>")
                .replaceAll("&7", "<gray>")
                .replaceAll("&8", "<dark_gray>")
                .replaceAll("&9", "<blue>")
                .replaceAll("&a", "<green>")
                .replaceAll("&b", "<aqua>")
                .replaceAll("&c", "<red>")
                .replaceAll("&d", "<light_purple>")
                .replaceAll("&e", "<yellow>")
                .replaceAll("&f", "<white>")
                .replaceAll("&k", "<reset><obf>")
                .replaceAll("&l", "<reset><b>")
                .replaceAll("&m", "<reset><st>")
                .replaceAll("&n", "<reset><underlined>")
                .replaceAll("&o", "<reset><em>")
                .replaceAll("&r", "<reset>");
        return MiniMessage.get().parse(text).asComponent();
    }

}
