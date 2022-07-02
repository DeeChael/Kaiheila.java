package net.deechael.khl.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.User;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import net.deechael.khl.message.ReceivedChannelMessage;
import net.deechael.khl.message.cardmessage.Card;
import net.deechael.khl.message.cardmessage.CardMessage;
import net.deechael.khl.message.cardmessage.Theme;
import net.deechael.khl.message.cardmessage.element.KMarkdownText;
import net.deechael.khl.message.cardmessage.element.PlainText;
import net.deechael.khl.message.cardmessage.module.Header;
import net.deechael.khl.message.cardmessage.module.Section;
import net.deechael.khl.message.kmarkdown.KMarkdownMessage;
import net.deechael.khl.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public final class CommandManager extends KaiheilaObject {

    private final CommandDispatcher<CommandSender> commandDispatcher;

    private final Map<String, CommandSettings> commands = new HashMap<>();

    private final Map<Pattern, String> patterns = new HashMap<>();

    public CommandManager(Gateway gateway) {
        super(gateway);
        this.commandDispatcher = new CommandDispatcher<>();
    }

    public void register(KaiheilaCommandBuilder commandBuilder) {
        String commandName = commandBuilder.getName();
        CommandSettings commandSettings = new CommandSettings(commandName);
        commandSettings.setPrefixes(commandBuilder.getPrefixes());
        commandSettings.setAliases(commandBuilder.getAliases());
        commandSettings.setRegex(commandBuilder.getRegex());
        this.commands.put(commandName, commandSettings);
        this.commandDispatcher.getRoot().addChild(commandBuilder.build());
        updatePatterns();
    }

    private void updatePatterns() {
        this.patterns.clear();
        for (Entry<String, CommandSettings> entry : this.commands.entrySet()) {
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

    public void execute(ReceivedChannelMessage receivedChannelMessage) {
        String message = receivedChannelMessage.getContent();
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
        for (Entry<Pattern, String> entry : this.patterns.entrySet()) {
            if (entry.getKey().matcher(partToBeChecked).matches()) {
                try {
                    this.commandDispatcher.execute(entry.getValue() + message.substring(partToBeChecked.length()), new CommandSender(this.getGateway(), receivedChannelMessage));
                } catch (CommandSyntaxException e) {
                    CardMessage msg = new CardMessage();
                    Card card = new Card();
                    card.setTheme(Theme.DANGER);
                    PlainText error = new PlainText();
                    error.setContent("错误");
                    Header header = new Header();
                    header.setText(error);
                    card.append(header);
                    KMarkdownText content = new KMarkdownText();
                    content.setContent(KMarkdownMessage.mentionUser(receivedChannelMessage.getAuthor()).expendSpace(KMarkdownMessage.create(e.getMessage())));
                    Section section = new Section();
                    section.setText(content);
                    card.append(section);
                    msg.append(card);
                    receivedChannelMessage.getChannel().sendMessage(msg);
                }
            }
        }
    }

}
