package net.deechael.khl.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.deechael.khl.api.User;

public final class Command {

    public static LiteralArgumentBuilder<CommandSender> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(ArgumentType<T> argumentType, String name) {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }

    private Command() {}

}
