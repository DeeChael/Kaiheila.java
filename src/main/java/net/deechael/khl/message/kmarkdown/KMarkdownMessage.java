package net.deechael.khl.message.kmarkdown;

import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Role;
import net.deechael.khl.api.User;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.MessageTypes;

import java.util.ArrayList;
import java.util.List;

public class KMarkdownMessage implements Message {
    private final String content;

    private final List<KMarkdownMessage> expansion = new ArrayList<>();

    private KMarkdownMessage(String content) {
        this.content = content;
    }

    public static KMarkdownMessage bold(String value) {
        return new KMarkdownMessage(String.format("**%s**", value));
    }

    public static KMarkdownMessage italic(String value) {
        return new KMarkdownMessage(String.format("*%s*", value));
    }

    public static KMarkdownMessage boldItalic(String value) {
        return new KMarkdownMessage(String.format("***%s***", value));
    }

    public static KMarkdownMessage strike(String value) {
        return new KMarkdownMessage(String.format("~~%s~~", value));
    }

    public static KMarkdownMessage code(String value) {
        return new KMarkdownMessage(String.format("`%s`", value));
    }

    public static KMarkdownMessage codeBlock(String value) {
        return new KMarkdownMessage(String.format("```%s```", value));
    }

    public static KMarkdownMessage quote(String value) {
        return new KMarkdownMessage("> " + value.replace("\n", "\n> ") + "\n\n");
    }

    public static KMarkdownMessage divider(String value) {
        return new KMarkdownMessage("---");
    }

    public static KMarkdownMessage underline(String value) {
        return new KMarkdownMessage(String.format("(ins)%s(ins)", value));
    }

    public static KMarkdownMessage spoiler(String value) {
        return new KMarkdownMessage(String.format("(spl)%s(spl)", value));
    }

    public static KMarkdownMessage emoji(String value) {
        return new KMarkdownMessage(String.format(":%s:", value));
    }

    public static KMarkdownMessage guildEmojiId(String value) {
        return new KMarkdownMessage(String.format("[%s]", value));
    }

    public static KMarkdownMessage guildEmojiName(String value) {
        return new KMarkdownMessage(String.format("(emj)%s(emj)", value));
    }

    public static KMarkdownMessage channel(Channel channel) {
        return new KMarkdownMessage(String.format("(chn)%s(chn)", channel.getId()));
    }

    public static KMarkdownMessage mentionAll() {
        return new KMarkdownMessage("(met)all(met)");
    }

    public static KMarkdownMessage mentionHere() {
        return new KMarkdownMessage("(met)here(met)");
    }

    public static KMarkdownMessage mentionUser(User user) {
        return new KMarkdownMessage(String.format("(met)%s(met)", user.getId()));
    }

    public static KMarkdownMessage role(Role role) {
        return new KMarkdownMessage(String.format("(rol)%s(rol)", role.getId()));
    }

    public static KMarkdownMessage link(String value, String url) {
        return new KMarkdownMessage(String.format("[%s](%s)", value, url));
    }

    public static KMarkdownMessage empty() {
        return new KMarkdownMessage("");
    }

    public static KMarkdownMessage create(String content) {
        return new KMarkdownMessage(content);
    }

    public KMarkdownMessage expendLine(KMarkdownMessage markdown) {
        expansion.add(new KMarkdownMessage("\n" + markdown.getContent()));
        return this;
    }

    public KMarkdownMessage expend(KMarkdownMessage markdown) {
        expansion.add(new KMarkdownMessage(markdown.getContent()));
        return this;
    }

    public KMarkdownMessage expendSpace(KMarkdownMessage markdown) {
        expansion.add(new KMarkdownMessage(" " + markdown.getContent()));
        return this;
    }

    @Override
    public String getContent() {
        StringBuilder base = new StringBuilder(this.content);
        for (KMarkdownMessage message : this.expansion) {
            base.append(message.getContent());
        }
        return base.toString();
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.KMD;
    }

}
