package net.deechael.khl.entity.message.cardmessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardMessage implements Serializable {
    private final JSONArray modules = new JSONArray();
    private String type = "card";
    private Theme theme = Theme.Secondary;
    private Color color = Color.WHITE;
    private Size size = Size.LG;

    public static CardMessage create() {
        return new CardMessage();
    }

    public CardMessage setSize(Size size) {
        this.size = size;
        return this;
    }

    public CardMessage setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public CardMessage setColor(Color color) {
        this.theme = Theme.None;
        this.color = color;
        return this;
    }

    public CardMessage setType(String type) {
        this.type = type;
        return this;
    }

    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put("type", type);
            json.put("theme", theme.toString());
            if (theme == Theme.None) json.put("color", color.getRGB());
            json.put("size", size.toString());
            json.put("modules", modules);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public CardMessage add(Module module) {
        this.modules.put(module.toJSON());
        return this;
    }

    public enum Theme {
        Primary,
        Secondary,
        Success,
        Warning,
        Danger,
        Info,
        None("");
        public final String value;

        Theme() {
            this.value = this.name().toLowerCase();
        }

        Theme(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum Size {
        SM,
        LG;
        public final String value;

        Size() {
            this.value = this.name().toLowerCase();
        }

        @Override
        public String toString() {
            return value;
        }
    }

    private interface Structable extends Serializable {
    }

    private interface Textable extends Serializable {
    }

    private interface Accessoriable extends Serializable {
    }

    private interface Contentable extends Serializable {
    }

    public static abstract class Module implements Serializable {
        public static Header Header() {
            return new Header();
        }

        public static Section Section() {
            return new Section();
        }

        public static ImageGroup ImageGroup() {
            return new ImageGroup();
        }

        public static Container Container() {
            return new Container();
        }

        public static ActionGroup ActionGroup() {
            return new ActionGroup();
        }

        public static Content Content() {
            return new Content();
        }

        public static Divider Divider() {
            return new Divider();
        }

        public static Media.File File() {
            return new Media.File();
        }

        public static Media.Audio Audio() {
            return new Media.Audio();
        }

        public static Media.Video Video() {
            return new Media.Video();
        }

        public static Countdown Countdown() {
            return new Countdown();
        }

        public static Invite Invite() {
            return new Invite();
        }


        public static class Header extends Module {
            private static final String type = "header";
            private Element.PlainText text;

            public Header setText(Element.PlainText text) {
                this.text = text;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("text", text.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Section extends Module {
            private static final String type = "section";
            private Mode mode;
            private Textable text;
            private Accessoriable accessory;

            public Section setMode(Mode mode) {
                this.mode = mode;
                return this;
            }

            public Section setText(Textable text) {
                this.text = text;
                return this;
            }

            public Section setAccessory(Accessoriable accessory) {
                this.accessory = accessory;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("mode", mode.toString());
                    json.put("text", text.toJSON());
                    json.put("accessory", accessory.toJSON());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            public enum Mode {
                Left,
                Right;
                public final String value;

                Mode() {
                    this.value = this.name().toLowerCase();
                }
            }
        }

        public static class ImageGroup extends Module {
            private static final String type = "image-group";
            private final List<Element.Image> elements = new ArrayList<>();

            public ImageGroup add(Element.Image image) {
                elements.add(image);
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("elements", elements.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Container extends Module {
            private static final String type = "container";
            private final List<Element.Image> elements = new ArrayList<>();

            public Container add(Element.Image image) {
                elements.add(image);
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("elements", elements.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class ActionGroup extends Module {
            private static final String type = "action-group";
            private final List<Element.Button> elements = new ArrayList<>();

            public ActionGroup add(Element.Button button) {
                elements.add(button);
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("elements", elements.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Content extends Module implements Structable {
            private static final String type = "context";
            private final List<Contentable> elements = new ArrayList<>();

            public Content add(Contentable content) {
                elements.add(content);
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("elements", elements.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Divider extends Module {
            private static final String type = "divider";

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Media extends Module {
            private String src;
            private String title;

            public Media setSrc(String src) {
                this.src = src;
                return this;
            }

            public Media setTitle(String title) {
                this.title = title;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("src", src);
                    json.put("title", title);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            public static class File extends Media {
                private static final String type = "file";

                @Override
                public JSONObject toJSON() {
                    JSONObject json = super.toJSON();
                    try {
                        json.put("type", type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return json;
                }
            }

            public static class Audio extends Media {
                private static final String type = "audio";
                private String cover;

                public Audio setCover(String cover) {
                    this.cover = cover;
                    return this;
                }

                @Override
                public JSONObject toJSON() {
                    JSONObject json = super.toJSON();
                    try {
                        json.put("type", type);
                        json.put("cover", cover);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return json;
                }
            }

            public static class Video extends Media {
                private static final String type = "video";

                @Override
                public JSONObject toJSON() {
                    JSONObject json = super.toJSON();
                    try {
                        json.put("type", type);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return json;
                }
            }
        }

        public static class Countdown extends Module {
            private static final String type = "countdown";
            private int startTime;
            private int endTime;
            private Mode mode;

            public Countdown setStartTime(int startTime) {
                this.startTime = startTime;
                return this;
            }

            public Countdown setEndTime(int endTime) {
                this.endTime = endTime;
                return this;
            }

            public Countdown setMode(Mode mode) {
                this.mode = mode;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("startTime", startTime);
                    json.put("endTime", endTime);
                    json.put("mode", mode.value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            public enum Mode {
                Day,
                Hour,
                Second;
                public final String value;

                Mode() {
                    this.value = this.name().toLowerCase();
                }
            }
        }

        public static class Invite extends Module {
            private static final String type = "invite";
            private String code;

            public Invite setCode(String code) {
                this.code = code;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }
    }

    public static abstract class Element implements Serializable {
        public static PlainText Text() {
            return new PlainText();
        }

        public static Image Image() {
            return new Image();
        }

        public static Button Button() {
            return new Button();
        }

        public static KMarkdown KMarkdown() {
            return new KMarkdown();
        }

        public static class PlainText extends Element implements Structable, Textable, Contentable {
            private static final String type = "plain-text";
            private String content;
            private boolean emoji;

            public PlainText setContent(String content) {
                this.content = content;
                return this;
            }

            public PlainText setEmoji(boolean emoji) {
                this.emoji = emoji;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("content", content);
                    json.put("emoji", emoji);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class KMarkdown extends Element implements Structable, Textable, Contentable {
            private static final String type = "kmarkdown";
            private String content;

            public KMarkdown setContent(String content) {
                this.content = content;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }
        }

        public static class Image extends Element implements Accessoriable, Contentable {
            private static final String type = "image";
            private String src;
            private String alt;
            private Size size;
            private boolean circle;

            public Image setSrc(String src) {
                this.src = src;
                return this;
            }

            public Image setAlt(String alt) {
                this.alt = alt;
                return this;
            }

            public Image setSize(Size size) {
                this.size = size;
                return this;
            }

            public Image setCircle(boolean circle) {
                this.circle = circle;
                return this;
            }

            @Override
            public JSONObject toJSON() {
                JSONObject json = new JSONObject();
                try {
                    json.put("type", type);
                    json.put("src", src);
                    json.put("alt", alt);
                    json.put("size", size.value);
                    json.put("circle", circle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json;
            }

            public enum Size {
                SM,
                LG;
                public final String value;

                Size() {
                    this.value = this.name().toLowerCase();
                }
            }
        }

        public static class Button extends Element implements Accessoriable {
            private static final String type = "button";
            private String text;
            private Theme theme;
            private String click;
            private String value;

            public Button setText(String text) {
                this.text = text;
                return this;
            }

            public Button setTheme(Theme theme) {
                this.theme = theme;
                return this;
            }

            public Button setClick(String click) {
                this.click = click;
                return this;
            }

            public Button setValue(String value) {
                this.value = value;
                return this;
            }

            @Override
            public JSONObject toJSON() throws JSONException {
                JSONObject json = new JSONObject();
                json.put("type", type);
                json.put("text", text);
                json.put("theme", theme.value);
                json.put("click", click);
                json.put("value", value);
                return json;
            }
        }
    }

    public static abstract class Struct implements Serializable {
        public static Paragraph paragraph() {
            return new Paragraph();
        }

        public static class Paragraph implements Textable {
            private static final String type = "paragraph";
            private final List<Structable> fields = new ArrayList<>();
            private int cols = 0;

            public Paragraph addCol(Structable fields) {
                this.fields.add(fields);
                cols++;
                return this;
            }

            @Override
            public JSONObject toJSON() throws JSONException {
                JSONObject json = new JSONObject();
                json.put("type", type);
                json.put("cols", cols);
                json.put("fields", new JSONArray(fields));
                return json;
            }
        }
    }

    @Override
    public String toString() {
        return toJSON().toString();
    }
}
