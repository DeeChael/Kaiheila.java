package net.deechael.khl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.message.cardmessage.Card;
import net.deechael.khl.message.cardmessage.CardMessage;
import net.deechael.khl.message.cardmessage.Theme;
import net.deechael.khl.message.cardmessage.element.KMarkdownText;
import net.deechael.khl.message.cardmessage.element.PlainText;
import net.deechael.khl.message.cardmessage.module.Header;
import net.deechael.khl.message.cardmessage.module.Section;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;
import net.deechael.khl.util.StringUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommandManager extends KaiheilaObject {

    private final CommandDispatcher<CommandSender> commandDispatcher;

    private final Map<String, CommandSettings> commmands = new HashMap<>();

    private final Map<Pattern, String> patterns = new HashMap<>();

    public CommandManager(KaiheilaBot kaiheilaBot) {
        super(kaiheilaBot);
        this.commandDispatcher = new CommandDispatcher<>();
    }

    public void register(KaiheilaCommandBuilder commandBuilder) {
        String commandName = commandBuilder.getName();
        CommandSettings commandSettings = new CommandSettings(commandName);
        commandSettings.setPrefixes(commandBuilder.getPrefixes());
        commandSettings.setAliases(commandBuilder.getAliases());
        commandSettings.setRegex(commandBuilder.getRegex());
        this.commmands.put(commandName, commandSettings);
        this.commandDispatcher.getRoot().addChild(commandBuilder.build());
        updatePatterns();
    }

    private void updatePatterns() {
        this.patterns.clear();
        for (Entry<String, CommandSettings> entry : this.commmands.entrySet()) {
            CommandSettings settings = entry.getValue();
            if (settings.getRegex() != null) {
                patterns.put(Pattern.compile(settings.getRegex()), entry.getKey());
            } else {
                String safeCommandName = StringUtil.safePattern(entry.getKey());
                for (String prefix : entry.getValue().getPrefixes()) {
                    patterns.put(Pattern.compile(prefix + safeCommandName), entry.getKey());
                    for (String alias : entry.getValue().getAliases()) {
                        patterns.put(Pattern.compile(prefix + StringUtil.safePattern(alias)), entry.getKey());
                    }
                }
            }
        }
    }

    public void execute(Channel channel, User user, String message) {
        for (Entry<Pattern, String> entry : this.patterns.entrySet()) {
            while (message.endsWith(" ")) {
                message = message.substring(0, message.length() - 1);
                if (message.length() == 0)
                    break;
            }
            String partToBeChecked;
            if (message.contains(" ")) {
                partToBeChecked = message.split(" ")[0];
            } else {
                partToBeChecked = message;
            }
            if (entry.getKey().matcher(partToBeChecked).matches()) {
                try {
                    this.commandDispatcher.execute(entry.getValue() + message.substring(partToBeChecked.length()), new CommandSender(this.getKaiheilaBot(), channel, user));
                } catch (CommandSyntaxException e) {
                    CardMessage msg = new CardMessage();
                    Card card = new Card();
                    card.setTheme(Theme.DANGER);
                    PlainText error = new PlainText();
                    error.setContent("错误");
                    card.append(new Header().setText(error));
                    KMarkdownText content = new KMarkdownText();
                    content.setContent(KMarkdownMessage.mentionUser(user).expendSpace(KMarkdownMessage.create(e.getMessage())));
                    card.append(new Section().setText(content));
                    msg.append(card);
                    channel.sendTempMessage(msg, user);
                }
            }
        }
    }

}
