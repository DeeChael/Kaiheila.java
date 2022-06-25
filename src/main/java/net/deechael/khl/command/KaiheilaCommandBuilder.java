package net.deechael.khl.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Set;

public class KaiheilaCommandBuilder extends ArgumentBuilder<CommandSender, KaiheilaCommandBuilder> {

    private final String name;

    private final Set<String> prefixes;

    private final Set<String> aliases;

    private final String regex;

    KaiheilaCommandBuilder(String name, Set<String> prefixes, Set<String> aliases, String regex) {
        this.name = name;
        this.prefixes = prefixes;
        this.aliases = aliases;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public String getRegex() {
        return regex;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    @Override
    protected KaiheilaCommandBuilder getThis() {
        return this;
    }

    @Override
    public LiteralCommandNode<CommandSender> build() {
        final LiteralCommandNode<CommandSender> result = new LiteralCommandNode<>(getName(), getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for (final CommandNode<CommandSender> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }

}
