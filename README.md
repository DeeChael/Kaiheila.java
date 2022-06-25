# Kaiheila.java (WIP)
## 一个为开黑啦开发制作的Java SDK
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.deechael/khl/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.deechael/khl)

## !!!注意!!! 需要 JDK 17 以上

仍在开发中\
该项目为 https://github.com/FightingGuys/rabbit 及其子分支 https://github.com/Kamikuz/rabbit-enhanced 的新分支\
但是这个分支会更像 khl.py (python的sdk): https://github.com/TWT233/khl.py

吐槽一下，rabbit-enhanced新加的东西的结构有点难改。。实在受不了


### 其实现在SDK已经是可用情况了，不过仍然需要完善

### 使用:
#### Maven
```xml
<dependency>
    <groupId>net.deechael</groupId>
    <artifactId>khl</artifactId>
    <version>1.01.0</version>
    <scope>compile</scope>
</dependency>
```
#### Gradle
```kotlin
dependencies { 
    //...
    implementation 'net.deechael:khl:1.01.0'
}
```

## 使用案例
```java
public class Example {
    public static void main(String[] args) {
        // 非必须，只是为了防止意外泄露机器人的token
        // 配置文件API来源于BukkitAPI
        FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File("config.yml"));
        String apiToken = configuration.getString("token");

        // 创建一个新机器人实例
        KaiheilaBot bot = (KaiheilaBot) KaiheilaBotBuilder.builder()
                .createDefault(apiToken) // 使用默认配置构建 KaiheilaBot 实例
                .build(); // 创建实例
        
        // 注册事件监听器
        bot.addEventListener(new UserEventHandler());
        
        // 指令API，使用Mojang开发的brigadier库
        // 创建一个名为khljava的指令
        KaiheilaCommandBuilder command = Command.create("khljava").literal();
        // 当用户仅输入“.khljava”使调用该方法
        command.executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            channel.sendTempMessage("你调用了khljava指令！", user, false); // 向用户发送一条临时消息（仅该用户可见）
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        });
        // 定参指令，如用户输入“.khljava test”则会调用该方法
        command.then(Command.create("test").literal().executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            channel.sendTempMessage("你调用了khljava test指令！", user, false); // 向用户发送一条临时消息（仅该用户可见）
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        }));
        // 变参指令，如用户输入“.khljava @用户”则会调用该方法
        // 其他参数类型还有ChannelArgumentType, RoleArgumentType (Kaiheila.java提供)
        // StringArgumentType, IntegerArgumentType, LongArgumentType, DoubleArgumentType, FloatArgumentType, BoolArgumentType (Brigadier自带)
        command.then(Command.create("user" /* 此处为参数名，用以获取变量用，不可重复 */).argument(UserArgumentType.user(bot) /* 获取参数类型的对象 */).executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            User mentionedUser = UserArgumentType.getUser(context, "user"); // 获取参数输入的变量
            channel.sendTempMessage("你调用了khljava @Someuser指令！并且输入了一名用户，其名为：" + mentionedUser.getUsername(), user, false); // 向用户发送一条临时消息（仅该用户可见）
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        }));
        // 注册指令，默认前缀为“.”
        // 在创建指令时：
        // Command.create("指令名称").addPrefix(".").addPrefix("/").literal(); 来使用别的前缀
        // Command.create("指令名称").addAlias("aa").addAlias("bb").literal(); 来添加别名
        // Command.create("指令名称").withRegex("正则"); 来使用Regex （此时别名和前缀就没有用处了)
        bot.getCommandManager().register(command);

        // 运行机器人
        if (bot.start()) {

            // 运行异步任务，但是，现在用不了，不知道为什么，以后修
            bot.getScheduler().runTaskAsynchronously(() -> {
                // Do sth
            });
        } else {
            // 使用Logger输出运行失败
        }
    }

    /**
     * 创建用户事件处理器
     */
    public static class UserEventHandler extends EventListener {
        Logger logger = LoggerFactory.getLogger(UserEventHandler.class);
        /**
         * 接收消息更新事件
         *
         * @param bot KaiheilaBot 实例
         * @param event 更新文本消息事件内容
         */
        @Override
        public void onUpdateMessageEvent(Bot bot, UpdateMessageEvent event) {
            logger.info("[{}]{}",event.getEventAuthorId(), event.getEventContent());
            event.getChannel().sendMessage("You updated message", false);
        }
    }
}
```