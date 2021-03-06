import net.deechael.khl.api.Game;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.bot.KaiheilaBotBuilder;
import net.deechael.khl.command.Command;
import net.deechael.khl.command.CommandSender;
import net.deechael.khl.command.argument.ChannelArgumentType;
import net.deechael.khl.command.argument.MessageArgumentType;
import net.deechael.khl.command.argument.RoleArgumentType;
import net.deechael.khl.command.argument.UserArgumentType;
import net.deechael.khl.configuration.file.FileConfiguration;
import net.deechael.khl.configuration.file.YamlConfiguration;
import net.deechael.khl.event.EventHandler;
import net.deechael.khl.event.Listener;
import net.deechael.khl.event.channel.UpdateMessageEvent;
import net.deechael.khl.message.TextMessage;
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
                .createDefault(apiToken) // 使用默认配置构建 KaiheilaBot 实例
                .build(); // 创建实例
        // 添加事件处理器
        bot.addEventListener(new UserEventHandler());

        // 指令注册以及应用，使用mojang开源的brigadier库，minecraft 1.13+以后的指令系统均基于该项目开发
        bot.getCommandManager().register(Command.create("test").withPrefix(".").literal().executes(context -> {
            CommandSender sender = context.getSource();
            sender.getChannel().sendTempMessage("You just invoked \"test\" command", sender.getUser(), false);
            return 1;
        }).then(Command.create("user").argument(UserArgumentType.user(bot)).executes(context -> {
            CommandSender sender = context.getSource();
            sender.getChannel().sendTempMessage("你输入了一个用户：" + UserArgumentType.getUser(context, "user").getUsername(), sender.getUser().getId(), false);
            return 1;
                }))
                .then(Command.create("channel").argument(ChannelArgumentType.channel(bot)).executes(context -> {
                    CommandSender sender = context.getSource();
                    sender.getChannel().sendTempMessage("你输入了一个频道：" + ChannelArgumentType.getChannel(context, "channel").getName(), sender.getUser().getId(), false);
                    return 1;
                }))
                        .then(Command.create("role").argument(RoleArgumentType.role(bot)).executes(context -> {
                    CommandSender sender = context.getSource();
                    sender.getChannel().sendTempMessage("你输入了一个角色：" + RoleArgumentType.getRole(context, "role").getName(), sender.getUser().getId(), false);
                    return 1;
                }))
        );
        bot.getCommandManager().register(Command.create("aaaa").literal().then(Command.create("msg").argument(MessageArgumentType.message()).executes(context -> {
            CommandSender sender = context.getSource();
            TextMessage message = MessageArgumentType.getMessage(context, "msg");
            String content = message.getContent();
            sender.getChannel().sendTempMessage(new TextMessage("You sent: " + content), sender.getUser());
            return 1;
        })));
        bot.getCommandManager().register(Command.create("fff").withRegex("(\\.|。|/){1}(fff|ff|f){1}").literal().executes(context -> {
            CommandSender sender = context.getSource();
            sender.getChannel().sendTempMessage(new TextMessage("Hello!"), sender.getUser());
            return 1;
        }));

        // 登录实例
        Log.info("Logging...");
        if (bot.start()) {
            Log.info("Logged in successfully");

            // 游戏相关API请求过程比较缓慢，请耐心等待
            // 创建游戏
            Game kaiheilaJava = bot.createGame("Kaiheila.java");
            // 设置正在游玩的游戏
            bot.play(kaiheilaJava);

            // 上传文件
            File file = new File("self.jpg");
            System.out.println(bot.uploadAsset(file));

            // 创建任务，但是，现在不会成功触发，不知道为什么，以后修
            bot.getScheduler().runTaskAsynchronously(() -> {
                System.out.println("aaaa");
            });
        } else {
            Log.error("Failed to log in");
        }
    }

    /**
     * 创建用户事件处理器
     */
    public static class UserEventHandler implements Listener {
        Logger Log = LoggerFactory.getLogger(UserEventHandler.class);

        @EventHandler
        public void youCanRenameTheMethodAsYouWant(UpdateMessageEvent event) {
            Log.info("[{}]{}",event.getEventAuthorId(), event.getEventContent());
            event.getChannel().sendMessage("You sent message", false);
        }
    }

}