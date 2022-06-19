package net.deechael.khl.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.command.CommandExceptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class UserArgumentType implements ArgumentType<User> {

    private static final Collection<String> EXAMPLES = Arrays.asList("(met)1000(met)", "(met)982587531(met)", "(met)7777(met)");

    private final KaiheilaBot kaiheilaBot;

    private UserArgumentType(KaiheilaBot kaiheilaBot) {
        this.kaiheilaBot = kaiheilaBot;
    }

    public static User getUser(final CommandContext<?> context, final String name) {
        return context.getArgument(name, User.class);
    }

    public static UserArgumentType user(KaiheilaBot kaiheilaBot) {
        return new UserArgumentType(kaiheilaBot);
    }

    @Override
    public User parse(StringReader reader) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("\\(met\\)(\\d*)\\(met\\)");
        while (reader.canRead()) {
            result.append(reader.read());
            if (pattern.matcher(result.toString()).matches()) {
                break;
            }
        }
        if (!(result.toString().startsWith("(met)") && result.toString().endsWith("(met)"))) {
            throw CommandExceptions.NOT_A_USER.createWithContext(reader);
        }
        result = new StringBuilder(result.substring(5, result.length() - 5));
        User user = this.kaiheilaBot.getCacheManager().getUserCache().getElementById(result.toString());
        if (user == null) {
            throw CommandExceptions.USER_CANNOT_BE_FOUND.createWithContext(reader);
        }
        return user;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
