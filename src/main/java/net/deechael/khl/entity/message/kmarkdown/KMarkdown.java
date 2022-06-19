package net.deechael.khl.entity.message.kmarkdown;

import net.deechael.khl.api.User;

public class KMarkdown {
  public String value;

  public KMarkdown nextLine(KMarkdown markdown) {
    this.value += "\n" + markdown.value;
    return this;
  }

  public KMarkdown next(KMarkdown markdown) {
    this.value += " " + markdown.value;
    return this;
  }

  public static class Bold extends KMarkdown {
    private Bold(String value) {
      this.value = String.format("**%s**", value);
    }

    public static Bold create(String value) {
      return new Bold(value);
    }
  }

  public static class Italic extends KMarkdown {

    private Italic(String value) {
      this.value = String.format("*%s*", value);
    }

    public static Italic create(String value) {
      return new Italic(value);
    }
  }

  public static class BoldItalic extends KMarkdown {
    private BoldItalic(String value) {
      this.value = String.format("***%s***", value);
    }

    public static BoldItalic create(String value) {
      return new BoldItalic(value);
    }
  }

  public static class Strike extends KMarkdown {

    private Strike(String value) {
      this.value = String.format("~~%s~~", value);
    }

    public static Strike create(String value) {
      return new Strike(value);
    }
  }

  public static class Code extends KMarkdown {

    private Code(String value) {
      this.value = String.format("`%s`", value);
    }

    public static Code create(String value) {
      return new Code(value);
    }
  }

  public static class CodeBlock extends KMarkdown {
    String code = "";

    public static CodeBlock create(String code) {
      return new CodeBlock();
    }

    public CodeBlock appendLine(String code) {
      this.code += "\n" + code;
      return this;
    }

    public String getCode() {
      return String.format("```%s```", code);
    }
  }

  public static class Quote extends KMarkdown {

    private Quote(String value) {
      this.value = String.format("> %s", value);
    }

    public static Quote create(String value) {
      return new Quote(value);
    }

    public Quote next(String value) {
      this.value = this.value + "\n" + String.format("> %s", value);
      return this;
    }

    public Quote end() {
      this.value = this.value + "\n\n";
      return this;
    }
  }

  public static class Divider extends KMarkdown {

    private Divider() {
      this.value = "---";
    }

    public static Divider hr() {
      return new Divider();
    }
  }

  public static class Underline extends KMarkdown {

    private Underline(String value) {
      this.value = String.format("(ins)%s(ins)", value);
    }

    public static Underline create(String value) {
      return new Underline(value);
    }
  }

  public static class HeiMu extends KMarkdown {

    private HeiMu(String value) {
      this.value = String.format("(spl)%s(spl)", value);
    }

    public static HeiMu create(String value) {
      return new HeiMu(value);
    }

  }

  public static class Emoji extends KMarkdown {

    private Emoji(String value) {
      this.value = String.format(":%s:", value);
    }

    public static Emoji create(String value) {
      return new Emoji(value);
    }
  }

  public static class GuildEmoji extends KMarkdown {

    private GuildEmoji(String value) {
      this.value = value;
    }

    public static GuildEmoji withID(String value) {
      return new GuildEmoji(String.format("[%s]", value));
    }

    public static GuildEmoji withName(String value) {
      return new GuildEmoji(String.format("(emj)%s(emj)", value));
    }

  }

  public static class Channel extends KMarkdown {

    private Channel(String value) {
      this.value = String.format("(chn)%s(chn)", value);
    }

    public static Channel create(String value) {
      return new Channel(value);
    }

  }

  public static class Mention extends KMarkdown {

    private Mention(String value) {
      this.value = String.format("(met)%s(met)", value);
    }

    public static Mention all() {
      return new Mention("all");
    }

    public static Mention online() {
      return new Mention("here");
    }

    public static Mention user(User user) {
      return new Mention(user.getId());
    }
  }

  public static class Role extends KMarkdown {

    private Role(String value) {
      this.value = String.format("(rol)%s(rol)", value);
    }

    public static Role create(String value) {
      return new Role(value);
    }
  }

  public static class Link extends KMarkdown {
    private Link(String value, String url) {
      this.value = String.format("[%s](%s)", value, url);
    }

    public static Link create(String text, String url) {
      return new Link(text, url);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
