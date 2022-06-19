package net.deechael.khl.hook;

import net.deechael.khl.api.Bot;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.event.FailureEvent;
import net.deechael.khl.event.IEvent;
import net.deechael.khl.event.UnknownEvent;
import net.deechael.khl.event.channel.*;
import net.deechael.khl.event.guild.AddedBlockListEvent;
import net.deechael.khl.event.guild.DeletedBlockListEvent;
import net.deechael.khl.event.guild.DeletedGuildEvent;
import net.deechael.khl.event.guild.UpdatedGuildEvent;
import net.deechael.khl.event.dm.DeletedPrivateMessageEvent;
import net.deechael.khl.event.dm.PrivateAddedReactionEvent;
import net.deechael.khl.event.dm.PrivateDeletedReactionEvent;
import net.deechael.khl.event.dm.UpdatedPrivateMessageEvent;
import net.deechael.khl.event.member.*;
import net.deechael.khl.event.message.*;
import net.deechael.khl.event.role.AddedRoleEvent;
import net.deechael.khl.event.role.DeletedRoleEvent;
import net.deechael.khl.event.role.UpdatedRoleEvent;
import net.deechael.khl.event.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public abstract class EventListener {

    private static final Logger Log = LoggerFactory.getLogger(EventListener.class);

    // @formatter:off
    public void onEvent(Bot bot, IEvent event) {}
    public void onUnknownEvent(Bot bot, UnknownEvent event) {}
    public void onFailureEvent(Bot bot, FailureEvent event) {}

    public void onCardMessageEvent(Bot bot, CardMessageEvent event) {}
    public void onFileMessageEvent(Bot bot, FileMessageEvent event) {}
    public void onImageMessageEvent(Bot bot, ImageMessageEvent event) {}
    public void onMarkDownMessageEvent(Bot bot, MarkDownMessageEvent event) {}
    public void onTextMessageEvent(Bot bot, TextMessageEvent event) {}
    public void onVideoMessageEvent(Bot bot, VideoMessageEvent event) {}

    public void onAddedChannelEvent(Bot bot, AddedChannelEvent event) {}
    public void onAddedReactionEvent(Bot bot, AddedReactionEvent event) {}
    public void onDeletedChannelEvent(Bot bot, DeletedChannelEvent event) {}
    public void onDeletedMessageEvent(Bot bot, DeletedMessageEvent event) {}
    public void onDeletedReactionEvent(Bot bot, DeletedReactionEvent event) {}
    public void onPinnedMessageEvent(Bot bot, PinnedMessageEvent event) {}
    public void onUnPinnedMessageEvent(Bot bot, UnPinnedMessageEvent event) {}
    public void onUpdatedChannelEvent(Bot bot, UpdatedChannelEvent event) {}
    public void onUpdateMessageEvent(Bot bot, UpdateMessageEvent event) {}

    public void onAddedBlockListEvent(Bot bot, AddedBlockListEvent event) {}
    public void onDeletedBlockListEvent(Bot bot, DeletedBlockListEvent event) {}
    public void onDeletedGuildEvent(Bot bot, DeletedGuildEvent event) {}
    public void onUpdatedGuildEvent(Bot bot, UpdatedGuildEvent event) {}

    public void onExitedGuildEvent(Bot bot, ExitedGuildEvent event) {}
    public void onGuildMemberOfflineEvent(Bot bot, GuildMemberOfflineEvent event) {}
    public void onGuildMemberOnlineEvent(Bot bot, GuildMemberOnlineEvent event) {}
    public void onJoinedGuildEvent(Bot bot, JoinedGuildEvent event) {}
    public void onUpdatedGuildMemberEvent(Bot bot, UpdatedGuildMemberEvent event) {}

    public void onDeletedPrivateMessageEvent(Bot bot, DeletedPrivateMessageEvent event) {}
    public void onPrivateAddedReactionEvent(Bot bot, PrivateAddedReactionEvent event) {}
    public void onPrivateDeletedReactionEvent(Bot bot, PrivateDeletedReactionEvent event) {}
    public void onUpdatedPrivateMessageEvent(Bot bot, UpdatedPrivateMessageEvent event) {}

    public void onAddedRoleEvent(Bot bot, AddedRoleEvent event) {}
    public void onDeletedRoleEvent(Bot bot, DeletedRoleEvent event) {}
    public void onUpdatedRoleEvent(Bot bot, UpdatedRoleEvent event) {}

    public void onExitedChannelEvent(Bot bot, ExitedChannelEvent event) {}
    public void onJoinedChannelEvent(Bot bot, JoinedChannelEvent event) {}
    public void onMessageBtnClickEvent(Bot bot, MessageBtnClickEvent event) {}
    public void onSelfExitedGuildEvent(Bot bot, SelfExitedGuildEvent event) {}
    public void onSelfJoinedGuildEvent(Bot bot, SelfJoinedGuildEvent event) {}
    public void onUserUpdatedEvent(Bot bot, UserUpdatedEvent event) {}
    public void onBotMessageEvent(Bot bot, BotMessageEvent event) {}
    // @formatter:on

    public final void handle(KaiheilaBot rabbit, IEvent event) {
        this.onEvent(rabbit, event);
        String methodName = "on" + event.getClass().getSimpleName();
        try {
            Method method = this.getClass().getMethod(methodName, Bot.class, event.getClass());
            method.invoke(this, rabbit, event);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.error(e.getClass().getName() + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
