package net.deechael.khl.hook;

import net.deechael.khl.RabbitImpl;
import net.deechael.khl.core.RabbitObject;
import net.deechael.khl.event.FailureEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.event.UnknownEvent;
import cn.fightingguys.kaiheila.event.channel.*;
import net.deechael.khl.event.channel.*;
import net.deechael.khl.event.dm.DeletedPrivateMessageEvent;
import net.deechael.khl.event.dm.PrivateAddedReactionEvent;
import net.deechael.khl.event.dm.PrivateDeletedReactionEvent;
import net.deechael.khl.event.dm.UpdatedPrivateMessageEvent;
import net.deechael.khl.event.guild.AddedBlockListEvent;
import net.deechael.khl.event.guild.DeletedBlockListEvent;
import net.deechael.khl.event.guild.DeletedGuildEvent;
import net.deechael.khl.event.guild.UpdatedGuildEvent;
import cn.fightingguys.kaiheila.event.member.*;
import cn.fightingguys.kaiheila.event.message.*;
import net.deechael.khl.event.message.*;
import net.deechael.khl.event.role.AddedRoleEvent;
import net.deechael.khl.event.role.DeletedRoleEvent;
import net.deechael.khl.event.role.UpdatedRoleEvent;
import cn.fightingguys.kaiheila.event.user.*;
import net.deechael.khl.hook.queue.SequenceMessageQueue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.deechael.khl.event.member.*;
import net.deechael.khl.event.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public class EventParser extends RabbitObject implements Runnable {
    protected static final Logger Log = LoggerFactory.getLogger(EventParser.class);
    private static final HashMap<String, Class<? extends IEvent>> eventClasses = new HashMap<>();

    private final Thread handleThread;
    private final SequenceMessageQueue<String> messageQueue;
    private final List<EventListener> listeners;

    public EventParser(RabbitImpl rabbit, SequenceMessageQueue<String> messageQueue, List<EventListener> listeners) {
        super(rabbit);
        this.messageQueue = messageQueue;
        this.listeners = listeners;
        this.handleThread = new Thread(this);
        this.handleThread.setName("EventParserThread");
        this.handleThread.setDaemon(true);
        this.handleThread.start();
    }

    public void shutdown() {
        if (handleThread != null) {
            handleThread.interrupt();
            try {
                handleThread.join();
            } catch (InterruptedException ignore) {
            }
        }
    }

    private void eventHandler(String data) {
        ObjectMapper jsonEngine = getRabbitImpl().getJsonEngine();
        JsonNode dataNode;
        try {
            dataNode = jsonEngine.readTree(data);
        } catch (JsonProcessingException ignored) {
            if (Log.isWarnEnabled()) {
                Log.warn("事件 JSON 数据解析失败 [{}]", data);
            }
            return;
        }
        IEvent event = createEventObject(dataNode);
        this.listeners.forEach(eventListener -> eventListener.handle(getRabbitImpl(), event));
    }

    private IEvent createEventObject(JsonNode dataNode) {
        int type = dataNode.get("type").asInt();
        if (type == 255) {
            String sType = dataNode.get("extra").get("type").asText();
            Class<? extends IEvent> clazz = eventClasses.get(sType);
            try {
                IEvent event = clazz.getConstructor(RabbitImpl.class, JsonNode.class).newInstance(getRabbitImpl(), dataNode);
                return event.handleSystemEvent(dataNode);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                Log.error(e.getMessage());
                return new FailureEvent(getRabbitImpl(), dataNode, e);
            }
        }
        switch (type) {
            case 1:
                return new TextMessageEvent(getRabbitImpl(), dataNode);
            case 2:
                return new ImageMessageEvent(getRabbitImpl(), dataNode);
            case 3:
                return new VideoMessageEvent(getRabbitImpl(), dataNode);
            case 4:
            case 8:
                return new FileMessageEvent(getRabbitImpl(), dataNode);
            case 9:
                return new MarkDownMessageEvent(getRabbitImpl(), dataNode);
            case 10:
                return new CardMessageEvent(getRabbitImpl(), dataNode);
            default:
                return new UnknownEvent(getRabbitImpl(), dataNode);
        }
    }

    public Thread getHandleThread() {
        return handleThread;
    }

    @Override
    public void run() {
        Log.debug("{} 线程启动", Thread.currentThread().getName());
        while (!Thread.interrupted()) {
            try {
                String take = this.messageQueue.take();
                eventHandler(take);
            } catch (InterruptedException e) {
                if (Log.isWarnEnabled()) {
                    Log.warn("{} 线程被中断", Thread.currentThread().getName());
                }
                break;
            }
        }
        if (Log.isWarnEnabled()) {
            Log.warn("{} 线程退出", Thread.currentThread().getName());
        }
    }

    static {
        //--Channel
        // 频道内用户添加 reaction
        eventClasses.put(AddedReactionEvent._AcceptType, AddedReactionEvent.class);
        // 频道内用户取消 reaction
        eventClasses.put(DeletedReactionEvent._AcceptType, DeletedReactionEvent.class);
        // 频道消息更新
        eventClasses.put(UpdateMessageEvent._AcceptType, UpdateMessageEvent.class);
        // 频道消息被删除
        eventClasses.put(DeletedMessageEvent._AcceptType, DeletedMessageEvent.class);
        // 新增频道
        eventClasses.put(AddedChannelEvent._AcceptType, AddedChannelEvent.class);
        // 修改频道信息
        eventClasses.put(UpdatedChannelEvent._AcceptType, UpdatedChannelEvent.class);
        // 删除频道
        eventClasses.put(DeletedChannelEvent._AcceptType, DeletedChannelEvent.class);
        // 新的频道置顶消息
        eventClasses.put(PinnedMessageEvent._AcceptType, PinnedMessageEvent.class);
        // 取消频道置顶消息
        eventClasses.put(UnPinnedMessageEvent._AcceptType, UnPinnedMessageEvent.class);

        //--Private chat
        // 私聊消息更新
        eventClasses.put(DeletedPrivateMessageEvent._AcceptType, DeletedPrivateMessageEvent.class);
        // 私聊消息被删除
        eventClasses.put(PrivateAddedReactionEvent._AcceptType, PrivateAddedReactionEvent.class);
        // 私聊内用户添加 reaction
        eventClasses.put(PrivateDeletedReactionEvent._AcceptType, PrivateDeletedReactionEvent.class);
        // 私聊内用户取消 reaction
        eventClasses.put(UpdatedPrivateMessageEvent._AcceptType, UpdatedPrivateMessageEvent.class);

        //--GuildUser
        // 新成员加入服务器
        eventClasses.put(JoinedGuildEvent._AcceptType, JoinedGuildEvent.class);
        // 服务器成员退出
        eventClasses.put(ExitedGuildEvent._AcceptType, ExitedGuildEvent.class);
        // 服务器成员信息更新
        eventClasses.put(UpdatedGuildMemberEvent._AcceptType, UpdatedGuildMemberEvent.class);
        // 服务器成员上线
        eventClasses.put(GuildMemberOnlineEvent._AcceptType, GuildMemberOnlineEvent.class);
        // 服务器成员下线
        eventClasses.put(GuildMemberOfflineEvent._AcceptType, GuildMemberOfflineEvent.class);

        //--GuildRole
        // 服务器角色增加
        eventClasses.put(AddedRoleEvent._AcceptType, AddedRoleEvent.class);
        // 服务器角色删除
        eventClasses.put(DeletedRoleEvent._AcceptType, DeletedRoleEvent.class);
        // 服务器角色更新
        eventClasses.put(UpdatedRoleEvent._AcceptType, UpdatedRoleEvent.class);

        //--Guild
        // 服务器信息更新
        eventClasses.put(UpdatedGuildEvent._AcceptType, UpdatedGuildEvent.class);
        // 服务器删除
        eventClasses.put(DeletedGuildEvent._AcceptType, DeletedGuildEvent.class);
        // 服务器封禁用户
        eventClasses.put(AddedBlockListEvent._AcceptType, AddedBlockListEvent.class);
        // 服务器取消封禁用户
        eventClasses.put(DeletedBlockListEvent._AcceptType, DeletedBlockListEvent.class);

        //--User
        // 用户加入语音频道
        eventClasses.put(JoinedChannelEvent._AcceptType, JoinedChannelEvent.class);
        // 用户退出语音频道
        eventClasses.put(ExitedChannelEvent._AcceptType, ExitedChannelEvent.class);
        // 用户信息更新
        eventClasses.put(UserUpdatedEvent._AcceptType, UserUpdatedEvent.class);
        // 自己新加入服务器
        eventClasses.put(SelfJoinedGuildEvent._AcceptType, SelfJoinedGuildEvent.class);
        // 自己退出服务器
        eventClasses.put(SelfExitedGuildEvent._AcceptType, SelfExitedGuildEvent.class);
        // Card 消息中的 Button 点击事件
        eventClasses.put(MessageBtnClickEvent._AcceptType, MessageBtnClickEvent.class);
    }

}
