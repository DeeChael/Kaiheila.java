package net.deechael.khl.message.cardmessage;

import com.google.gson.*;
import net.deechael.khl.message.Message;
import net.deechael.khl.message.MessageTypes;
import net.deechael.khl.message.cardmessage.element.Button;
import net.deechael.khl.message.cardmessage.element.Image;
import net.deechael.khl.message.cardmessage.element.KMarkdownText;
import net.deechael.khl.message.cardmessage.element.PlainText;
import net.deechael.khl.message.cardmessage.module.Container;
import net.deechael.khl.message.cardmessage.module.*;

import java.awt.*;
import java.util.Objects;

public class CardMessage implements Serializable, Message {

    private final JsonArray cards = new JsonArray();

    public static CardMessage parse(String json) {
        JsonElement base = JsonParser.parseString(json);
        System.out.println(json);
        if (base.isJsonArray()) {
            CardMessage message = new CardMessage();
            JsonArray cardMessageArray = base.getAsJsonArray();
            for (JsonElement element : cardMessageArray) {
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    Card card = new Card();
                    if (object.has("theme")) {
                        card.setTheme(Theme.valueOf(object.get("theme").getAsString().toUpperCase()));
                    }
                    if (object.has("color") && object.get("color").isJsonPrimitive() && object.getAsJsonPrimitive("color").isNumber()) {
                        card.setColor(new Color(object.get("color").getAsInt()));
                    }
                    if (object.has("size")) {
                        card.setSize(Size.valueOf(object.get("size").getAsString().toUpperCase()));
                    }
                    if (object.has("modules")) {
                        for (JsonElement moduleElement : object.getAsJsonArray("modules")) {
                            JsonObject moduleObject = moduleElement.getAsJsonObject();
                            String type = moduleObject.get("type").getAsString();
                            if (type.equals("action-group")) {
                                ActionGroup actionGroup = new ActionGroup();
                                for (JsonElement buttonElement : moduleObject.getAsJsonArray("elements")) {
                                    JsonObject buttonObject = buttonElement.getAsJsonObject();
                                    Button button = new Button();
                                    if (buttonObject.has("text")) {
                                        JsonObject textObject = buttonObject.getAsJsonObject("text");
                                        if (Objects.equals(textObject.get("type").getAsString(), "kmarkdown")) {
                                            KMarkdownText text = new KMarkdownText();
                                            if (textObject.has("content")) {
                                                text.setContent(textObject.get("content").getAsString());
                                            }
                                            button.setText(text);
                                        } else {
                                            PlainText text = new PlainText();
                                            if (textObject.has("content")) {
                                                text.setContent(textObject.get("content").getAsString());
                                            }
                                            if (textObject.has("emoji")) {
                                                text.setEmoji(textObject.get("emoji").getAsBoolean());
                                            }
                                            button.setText(text);
                                        }
                                    }
                                    if (buttonObject.has("theme")) {
                                        button.setTheme(Theme.valueOf(buttonObject.get("theme").getAsString().toUpperCase()));
                                    }
                                    if (buttonObject.has("click")) {
                                        button.setClick(Button.Click.valueOf(buttonObject.get("click").getAsString().toUpperCase()));
                                    }
                                    if (buttonObject.has("value")) {
                                        button.setValue(buttonObject.get("value").getAsString());
                                    }
                                    actionGroup.add(button);
                                }
                                card.append(actionGroup);
                            } else if (type.equals("audio")) {
                                Audio audio = new Audio();
                                if (moduleObject.has("src")) {
                                    audio.setSrc(moduleObject.get("src").getAsString());
                                }
                                if (moduleObject.has("title")) {
                                    audio.setTitle(moduleObject.get("title").getAsString());
                                }
                                if (moduleObject.has("cover")) {
                                    audio.setCover(moduleObject.get("cover").getAsString());
                                }
                                card.append(audio);
                            } else if (type.equals("container")) {
                                Container container = new Container();
                                for (JsonElement imageElement : moduleObject.getAsJsonArray("elements")) {
                                    JsonObject imageObject = imageElement.getAsJsonObject();
                                    Image image = new Image();
                                    if (imageObject.has("src")) {
                                        image.setSrc(imageObject.get("src").getAsString());
                                    }
                                    if (imageObject.has("alt")) {
                                        image.setAlt(imageObject.get("alt").getAsString());
                                    }
                                    if (imageObject.has("size")) {
                                        image.setSize(Size.valueOf(imageObject.get("size").getAsString().toUpperCase()));
                                    }
                                    if (imageObject.has("circle")) {
                                        image.setCircle(imageObject.get("circle").getAsBoolean());
                                    }
                                    container.add(image);
                                }
                                card.append(container);
                            } else if (type.equals("context")) {
                                Context context = new Context();
                                for (JsonElement contentElement : moduleObject.getAsJsonArray("elements")) {
                                    JsonObject contentObject = contentElement.getAsJsonObject();
                                    if (Objects.equals(contentObject.get("type").getAsString(), "image")) {
                                        Image image = new Image();
                                        if (contentObject.has("src")) {
                                            image.setSrc(contentObject.get("src").getAsString());
                                        }
                                        if (contentObject.has("alt")) {
                                            image.setAlt(contentObject.get("alt").getAsString());
                                        }
                                        if (contentObject.has("size")) {
                                            image.setSize(Size.valueOf(contentObject.get("size").getAsString().toUpperCase()));
                                        }
                                        if (contentObject.has("circle")) {
                                            image.setCircle(contentObject.get("circle").getAsBoolean());
                                        }
                                        context.add(image);
                                    } else if (Objects.equals(contentObject.get("type").getAsString(), "kmarkdown")) {
                                        KMarkdownText text = new KMarkdownText();
                                        if (contentObject.has("content")) {
                                            text.setContent(contentObject.get("content").getAsString());
                                        }
                                        context.add(text);
                                    } else if (Objects.equals(contentObject.get("type").getAsString(), "plain-text")) {
                                        PlainText text = new PlainText();
                                        if (contentObject.has("content")) {
                                            text.setContent(contentObject.get("content").getAsString());
                                        }
                                        if (contentObject.has("emoji")) {
                                            text.setEmoji(contentObject.get("emoji").getAsBoolean());
                                        }
                                        context.add(text);
                                    }
                                }
                                card.append(context);
                            } else if (type.equals("countdown")) {
                                Countdown countdown = new Countdown();
                                if (moduleObject.has("startTime")) {
                                    countdown.setStartTime(moduleObject.get("startTime").getAsLong());
                                }
                                if (moduleObject.has("endTime")) {
                                    countdown.setEndTime(moduleObject.get("endTime").getAsLong());
                                }
                                if (moduleObject.has("mode")) {
                                    countdown.setMode(Countdown.Mode.valueOf(moduleObject.get("mode").getAsString().toUpperCase()));
                                }
                                card.append(countdown);
                            } else if (type.equals("divider")) {
                                card.append(new Divider());
                            } else if (type.equals("file")) {
                                File file = new File();
                                if (moduleObject.has("src")) {
                                    file.setSrc(moduleObject.get("src").getAsString());
                                }
                                if (moduleObject.has("title")) {
                                    file.setTitle(moduleObject.get("title").getAsString());
                                }
                                card.append(file);
                            } else if (type.equals("header")) {
                                Header header = new Header();
                                if (moduleObject.has("text")) {
                                    JsonObject textObject = moduleObject.getAsJsonObject("text");
                                    if (Objects.equals(textObject.get("type").getAsString(), "kmarkdown")) {
                                        KMarkdownText text = new KMarkdownText();
                                        if (textObject.has("content")) {
                                            text.setContent(textObject.get("content").getAsString());
                                        }
                                        header.setText(text);
                                    } else {
                                        PlainText text = new PlainText();
                                        if (textObject.has("content")) {
                                            text.setContent(textObject.get("content").getAsString());
                                        }
                                        if (textObject.has("emoji")) {
                                            text.setEmoji(textObject.get("emoji").getAsBoolean());
                                        }
                                        header.setText(text);
                                    }
                                }
                                card.append(header);
                            } else if (type.equals("image-group")) {
                                ImageGroup imageGroup = new ImageGroup();
                                for (JsonElement imageElement : moduleObject.getAsJsonArray("elements")) {
                                    JsonObject imageObject = imageElement.getAsJsonObject();
                                    Image image = new Image();
                                    if (imageObject.has("src")) {
                                        image.setSrc(imageObject.get("src").getAsString());
                                    }
                                    if (imageObject.has("alt")) {
                                        image.setAlt(imageObject.get("alt").getAsString());
                                    }
                                    if (imageObject.has("size")) {
                                        image.setSize(Size.valueOf(imageObject.get("size").getAsString().toUpperCase()));
                                    }
                                    if (imageObject.has("circle")) {
                                        image.setCircle(imageObject.get("circle").getAsBoolean());
                                    }
                                    imageGroup.add(image);
                                }
                                card.append(imageGroup);
                            } else if (type.equals("invite")) {
                                if (moduleObject.has("code")) {
                                    card.append(new Invite(moduleObject.get("code").getAsString()));
                                }
                            } else if (type.equals("section")) {
                                Section section = new Section();
                                if (moduleObject.has("mode")) {
                                    section.setMode(Section.Mode.valueOf(moduleObject.get("mode").getAsString().toUpperCase()));
                                }
                                if (moduleObject.has("text")) {
                                    JsonObject textObject = moduleObject.getAsJsonObject("text");
                                    if (Objects.equals(textObject.get("type").getAsString(), "kmarkdown")) {
                                        KMarkdownText text = new KMarkdownText();
                                        if (textObject.has("content")) {
                                            text.setContent(textObject.get("content").getAsString());
                                        }
                                        section.setText(text);
                                    } else {
                                        PlainText text = new PlainText();
                                        if (textObject.has("content")) {
                                            text.setContent(textObject.get("content").getAsString());
                                        }
                                        if (textObject.has("emoji")) {
                                            text.setEmoji(textObject.get("emoji").getAsBoolean());
                                        }
                                        section.setText(text);
                                    }
                                }
                                if (moduleObject.has("accessory") && moduleObject.get("accessory").isJsonPrimitive() && !moduleObject.getAsJsonPrimitive("accessory").isJsonNull()) {
                                    JsonObject accessoryObject = moduleObject.getAsJsonObject("accessory");
                                    if (Objects.equals(accessoryObject.get("type").getAsString(), "button")) {
                                        Button button = new Button();
                                        if (accessoryObject.has("text")) {
                                            JsonObject textObject = accessoryObject.getAsJsonObject("text");
                                            if (Objects.equals(textObject.get("type").getAsString(), "kmarkdown")) {
                                                KMarkdownText text = new KMarkdownText();
                                                if (textObject.has("content")) {
                                                    text.setContent(textObject.get("content").getAsString());
                                                }
                                                button.setText(text);
                                            } else {
                                                PlainText text = new PlainText();
                                                if (textObject.has("content")) {
                                                    text.setContent(textObject.get("content").getAsString());
                                                }
                                                if (textObject.has("emoji")) {
                                                    text.setEmoji(textObject.get("emoji").getAsBoolean());
                                                }
                                                button.setText(text);
                                            }
                                        }
                                        if (accessoryObject.has("theme")) {
                                            button.setTheme(Theme.valueOf(accessoryObject.get("theme").getAsString().toUpperCase()));
                                        }
                                        if (accessoryObject.has("click")) {
                                            button.setClick(Button.Click.valueOf(accessoryObject.get("click").getAsString().toUpperCase()));
                                        }
                                        if (accessoryObject.has("value")) {
                                            button.setValue(accessoryObject.get("value").getAsString());
                                        }
                                        section.setAccessory(button);
                                    } else if (Objects.equals(accessoryObject.get("type").getAsString(), "image")) {
                                        Image image = new Image();
                                        if (accessoryObject.has("src")) {
                                            image.setSrc(accessoryObject.get("src").getAsString());
                                        }
                                        if (accessoryObject.has("alt")) {
                                            image.setAlt(accessoryObject.get("alt").getAsString());
                                        }
                                        if (accessoryObject.has("size")) {
                                            image.setSize(Size.valueOf(accessoryObject.get("size").getAsString().toUpperCase()));
                                        }
                                        if (accessoryObject.has("circle")) {
                                            image.setCircle(accessoryObject.get("circle").getAsBoolean());
                                        }
                                        section.setAccessory(image);
                                    }
                                }
                                card.append(section);
                            } else if (type.equals("video")) {
                                Video video = new Video();
                                if (moduleObject.has("src")) {
                                    video.setSrc(moduleObject.get("src").getAsString());
                                }
                                if (moduleObject.has("title")) {
                                    video.setTitle(moduleObject.get("title").getAsString());
                                }
                                card.append(video);
                            }
                            message.append(card);
                        }
                    }
                }
            }
            return message;
        }
        //TODO haven't finished yet
        throw new RuntimeException("Failed to parse card message");
    }

    public CardMessage append(Card card) {
        this.cards.add(card.asJson());
        return this;
    }

    @Override
    public String getContent() {
        return new Gson().toJson(this.asJson());
    }

    @Override
    public MessageTypes getType() {
        return MessageTypes.CARD;
    }

    @Override
    public JsonArray asJson() {
        return this.cards.deepCopy();
    }

}
