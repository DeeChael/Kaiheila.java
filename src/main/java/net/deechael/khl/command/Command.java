package net.deechael.khl.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import java.util.HashSet;
import java.util.Set;

public final class Command {

    private final String name;
    private final Set<String> prefixes = new HashSet<>();
    private final Set<String> aliases = new HashSet<>();
    private String regex = null;

    private Command(String name) {
        this.name = name;
    }

    public static Command create(String literal) {
        return new Command(literal);
    }

    public Command withAlias(String alias) {
        this.aliases.add(alias);
        return this;
    }

    public Command withPrefix(String prefix) {
        if (prefix.length() <= 0)
            return this;
        this.prefixes.add(prefix);
        return this;
    }

    public Command withRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public KaiheilaCommandBuilder literal() {
        if (this.prefixes.isEmpty()) {
            this.prefixes.add(".");
        }
        return new KaiheilaCommandBuilder(this.name, this.prefixes, this.aliases, this.regex);
    }

    public <T> RequiredArgumentBuilder<CommandSender, T> argument(ArgumentType<T> argumentType) {
        return RequiredArgumentBuilder.argument(this.name, argumentType);
    }

}
