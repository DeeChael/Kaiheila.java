package net.deechael.khl.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.deechael.khl.util.StringUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Command {

    private final String name;

    private String regex = null;

    private final Set<String> prefixes = new HashSet<>();

    private final Set<String> aliases = new HashSet<>();

    private Command(String name) {
        this.name = name;
    }

    public Command addAlias(String alias) {
        this.aliases.add(StringUtil.safePattern(alias));
        return this;
    }

    public Command addPrefix(String prefix) {
        if (prefix.length() <= 0)
            return this;
        this.prefixes.add(StringUtil.safePattern(prefix));
        return this;
    }

    public Command withRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public static Command create(String literal) {
        return new Command(literal);
    }

    public KaiheilaCommandBuilder literal() {
        return new KaiheilaCommandBuilder(this.name, this.prefixes, this.aliases, this.regex);
    }

    public <T> RequiredArgumentBuilder<CommandSender, T> argument(ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(this.name, argumentType);
    }

}
