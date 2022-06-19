package net.deechael.khl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class CommandManager extends KaiheilaObject {

    private final CommandDispatcher<CommandSender> commandDispatcher;

    private final Map<String, CommandSettings> commmands = new HashMap<>();

    private final Map<Pattern, String> patterns = new HashMap<>();

    public CommandManager(KaiheilaBot kaiheilaBot) {
        super(kaiheilaBot);
        this.commandDispatcher = new CommandDispatcher<>();
    }

    public void register(LiteralArgumentBuilder<CommandSender> literalArgumentBuilder) {
        String commandName = literalArgumentBuilder.getLiteral();
        if (!this.commmands.containsKey(commandName)) {
            this.commmands.put(commandName, new CommandSettings(commandName));
        }
        this.commandDispatcher.register(literalArgumentBuilder);
        updatePatterns();
    }

    public void register(LiteralCommandNode<CommandSender> literalCommandNode) {
        String commandName = literalCommandNode.getName();
        if (!this.commmands.containsKey(commandName)) {
            this.commmands.put(commandName, new CommandSettings(commandName));
        }
        this.commandDispatcher.getRoot().addChild(literalCommandNode);
        updatePatterns();
    }

    public void register(LiteralArgumentBuilder<CommandSender> literalArgumentBuilder, String[] prefixes) {
        String commandName = literalArgumentBuilder.getLiteral();
        if (!this.commmands.containsKey(commandName)) {
            this.commmands.put(commandName, new CommandSettings(commandName));
        }
        this.commmands.get(commandName).setPrefixes(Arrays.asList(prefixes));
        this.commandDispatcher.register(literalArgumentBuilder);
        updatePatterns();
    }

    public void register(LiteralCommandNode<CommandSender> literalCommandNode, String[] prefixes) {
        String commandName = literalCommandNode.getName();
        if (!this.commmands.containsKey(commandName)) {
            this.commmands.put(commandName, new CommandSettings(commandName));
        }
        this.commmands.get(commandName).setPrefixes(Arrays.asList(prefixes));
        this.commandDispatcher.getRoot().addChild(literalCommandNode);
        updatePatterns();
    }

    private void updatePatterns() {
        this.patterns.clear();
        for (Entry<String, CommandSettings> entry : this.commmands.entrySet()) {
            String safeCommandName = StringUtil.safePattern(entry.getKey());
            for (String prefix : entry.getValue().getPrefixes()) {
                patterns.put(Pattern.compile(prefix + safeCommandName + ".*"), StringUtil.unsafePattern(prefix));
            }
        }
    }

    public void execute(Channel channel, User user, String message) {
        for (Entry<Pattern, String> entry : this.patterns.entrySet()) {
            if (entry.getKey().matcher(message).matches()) {
                try {
                    this.commandDispatcher.execute(message.substring(this.patterns.get(entry.getKey()).length()), new CommandSender(this.getKaiheilaBot(), channel, user));
                } catch (CommandSyntaxException e) {
                    channel.getChannelOperation().sendTempMessage(e.getMessage(), user.getId(), false);
                }
            }
        }
    }

}
