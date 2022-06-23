package net.deechael.khl.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deechael.khl.api.Channel;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.command.CommandExceptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class ChannelArgumentType implements ArgumentType<Channel> {

    private static final Collection<String> EXAMPLES = Arrays.asList("(chn)1000(chn)", "(chn)982587531(chn)", "(chn)7777(chn)");

    private final KaiheilaBot kaiheilaBot;

    private ChannelArgumentType(KaiheilaBot kaiheilaBot) {
        this.kaiheilaBot = kaiheilaBot;
    }

    public static Channel getChannel(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Channel.class);
    }

    public static ChannelArgumentType channel(KaiheilaBot kaiheilaBot) {
        return new ChannelArgumentType(kaiheilaBot);
    }

    @Override
    public Channel parse(StringReader reader) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("\\(chn\\)(\\d*)\\(chn\\)");
        while (reader.canRead()) {
            result.append(reader.read());
            if (pattern.matcher(result.toString()).matches()) {
                break;
            }
        }
        if (!(result.toString().startsWith("(chn)") && result.toString().endsWith("(chn)"))) {
            throw CommandExceptions.NOT_A_CHANNEL.createWithContext(reader);
        }
        result = new StringBuilder(result.substring(5, result.length() - 5));
        Channel channel = this.kaiheilaBot.getCacheManager().getChannelCache().getElementById(result.toString());
        if (channel == null) {
            throw CommandExceptions.CHANNEL_CANNOT_BE_FOUND.createWithContext(reader);
        }
        return channel;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
