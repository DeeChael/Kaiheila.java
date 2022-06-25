package net.deechael.khl.command;

import net.deechael.khl.util.StringUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CommandSettings {

    private final String commandName;

    private final Set<String> prefixes = new HashSet<>();

    private final Set<String> aliases = new HashSet<>();

    private String regex = null;

    public CommandSettings(String commandName) {
        this.commandName = commandName;
        this.prefixes.add("\\.");
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public void setAliases(Collection<String> aliases) {
        this.aliases.clear();
        Set<String> checked = new HashSet<>();
        for (String prefix : aliases) {
            checked.add(StringUtil.safePattern(prefix));
        }
        this.aliases.addAll(checked);
    }

    public void addAlias(String alias) {
        this.aliases.add(StringUtil.safePattern(alias));
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(Collection<String> prefixes) {
        this.prefixes.clear();
        Set<String> checked = new HashSet<>();
        for (String prefix : aliases) {
            if (prefix.length() <= 0)
                continue;
            checked.add(StringUtil.safePattern(prefix));
        }
        this.prefixes.addAll(checked);
    }

    public void addPrefix(String prefix) {
        if (prefix.length() <= 0)
            return;
        this.prefixes.add(StringUtil.safePattern(prefix));
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

}
