package net.deechael.khl.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deechael.khl.message.TextMessage;

import java.util.Arrays;
import java.util.Collection;

public class MessageArgumentType implements ArgumentType<TextMessage> {

    private static final Collection<String> EXAMPLES = Arrays.asList("Hello worlds!", "foo", "Aaa");


    private MessageArgumentType() {}

    public static TextMessage getMessage(final CommandContext<?> context, final String name) {
        return context.getArgument(name, TextMessage.class);
    }

    public static MessageArgumentType message() {
        return new MessageArgumentType();
    }

    @Override
    public TextMessage parse(StringReader reader) throws CommandSyntaxException {
        TextMessage message = new TextMessage(reader.getString().substring(reader.getCursor(), reader.getTotalLength()));
        reader.setCursor(reader.getTotalLength());
        return message;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
