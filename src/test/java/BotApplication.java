import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.deechael.khl.api.Bot;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.bot.KaiheilaBotBuilder;
import net.deechael.khl.command.Command;
import net.deechael.khl.command.CommandSender;
import net.deechael.khl.command.argument.UserArgumentType;
import net.deechael.khl.configuration.file.FileConfiguration;
import net.deechael.khl.configuration.file.YamlConfiguration;
import net.deechael.khl.event.channel.UpdateMessageEvent;
import net.deechael.khl.hook.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 用户机器人应用
 */
public class BotApplication {

    public static void main(String[] args) {
        Logger Log = LoggerFactory.getLogger(BotApplication.class);
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File("config.yml"));
        String apiToken = configuration.getString("token");

        Log.info("Starting...");
        KaiheilaBot bot = (KaiheilaBot) KaiheilaBotBuilder.builder()
                .createDefault(apiToken) // 使用默认配置构建 Rabbit 实例
                .build(); // 创建实例
        // 添加事件处理器
        bot.addEventListener(new UserEventHandler());

        // 指令注册以及应用，使用mojang开源的bridgadier库，minecraft 1.13+以后的指令系统均基于该项目开发
        bot.getCommandManager().register(Command.literal("test").executes(context -> {
            CommandSender sender = context.getSource();
            sender.getChannel().getChannelOperation().sendTempMessage("You just invoked \"test\" command", sender.getUser().getId(), false);
            return 1;
        }).then(Command.argument(UserArgumentType.user(bot), "user").executes(context -> {
            CommandSender sender = context.getSource();
            sender.getChannel().getChannelOperation().sendTempMessage("The user you mentioned has his/her name: " + UserArgumentType.getUser(context, "user").getFullName(), sender.getUser().getId(), false);
            return 1;
                }).then(Command.argument(IntegerArgumentType.integer(), "age").executes(context -> {
                    CommandSender sender = context.getSource();
                    sender.getChannel().getChannelOperation().sendTempMessage("The user you mentioned has his/her name: " + UserArgumentType.getUser(context, "user").getFullName() + "\nYour age is " + IntegerArgumentType.getInteger(context, "age"), sender.getUser().getId(), false);
                    return 1;
                })))
        );

        // 登录实例
        Log.info("Logging...");
        if (bot.login()) {
            Log.info("Logged in successfully");
        } else {
            Log.error("Failed to log in");
        }
    }

    /**
     * 创建用户事件处理器
     */
    public static class UserEventHandler extends EventListener {
        Logger Log = LoggerFactory.getLogger(UserEventHandler.class);
        /**
         * 接收文本消息事件
         *
         * @param bot Rabbit 实例
         * @param event  文本消息事件内容
         */
        @Override
        public void onUpdateMessageEvent(Bot bot, UpdateMessageEvent event) {
            Log.info("[{}]{}",event.getEventAuthorId(), event.getEventContent());
            event.getChannel().getChannelOperation().broadcastMessage("You sent message", false);
        }
    }
}