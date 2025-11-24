package dev.keii.dungeons.util;

import java.util.Objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class MessageFormatter {
    private static Component format(NamedTextColor baseColor, String fmt, Object... args) {
        if (fmt == null) {
            return Component.text("null", baseColor);
        }

        if (args == null || args.length == 0) {
            return Component.text(fmt, baseColor);
        }

        TextComponent.Builder builder = Component.text();

        int idx = 0;
        int pos = 0;
        int p;

        while (idx < args.length && (p = fmt.indexOf("{}", pos)) != -1) {
            if (p > pos) {
                builder.append(Component.text(
                        fmt.substring(pos, p),
                        baseColor));
            }

            Object a = Objects.requireNonNullElse(args[idx++], "null");

            if (a instanceof ComponentLike like) {
                builder.append(like.asComponent().colorIfAbsent(NamedTextColor.YELLOW));
            } else {
                builder.append(Component.text(String.valueOf(a), NamedTextColor.YELLOW));
            }

            pos = p + 2;
        }

        if (pos < fmt.length()) {
            builder.append(Component.text(fmt.substring(pos), baseColor));
        }

        return builder.build();
    }

    public static Component error(String formatString, Object... params) {
        return format(NamedTextColor.RED, formatString, params);
    }

    public static Component success(String formatString, Object... params) {
        return format(NamedTextColor.GREEN, formatString, params);
    }
}
