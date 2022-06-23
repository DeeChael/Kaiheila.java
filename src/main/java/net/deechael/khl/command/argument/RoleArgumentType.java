package net.deechael.khl.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.deechael.khl.api.Role;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.command.CommandExceptions;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

public class RoleArgumentType implements ArgumentType<Role> {

    private static final Collection<String> EXAMPLES = Arrays.asList("(rol)1000(rol)", "(rol)982587531(rol)", "(rol)7777(rol)");

    private final KaiheilaBot kaiheilaBot;

    private RoleArgumentType(KaiheilaBot kaiheilaBot) {
        this.kaiheilaBot = kaiheilaBot;
    }

    public static Role getRole(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Role.class);
    }

    public static RoleArgumentType role(KaiheilaBot kaiheilaBot) {
        return new RoleArgumentType(kaiheilaBot);
    }

    @Override
    public Role parse(StringReader reader) throws CommandSyntaxException {
        StringBuilder result = new StringBuilder();
        Pattern pattern = Pattern.compile("\\(rol\\)(\\d*)\\(rol\\)");
        while (reader.canRead()) {
            result.append(reader.read());
            if (pattern.matcher(result.toString()).matches()) {
                break;
            }
        }
        if (!(result.toString().startsWith("(rol)") && result.toString().endsWith("(rol)"))) {
            throw CommandExceptions.NOT_A_USER.createWithContext(reader);
        }
        result = new StringBuilder(result.substring(5, result.length() - 5));
        Role role = this.kaiheilaBot.getCacheManager().getRoleCache().getElementById(Integer.parseInt(result.toString()));
        if (role == null) {
            throw CommandExceptions.USER_CANNOT_BE_FOUND.createWithContext(reader);
        }
        return role;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
