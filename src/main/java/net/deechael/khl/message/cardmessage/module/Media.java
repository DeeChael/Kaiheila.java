package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Media extends Module {

    private final String type;
    private String src;
    private String title;

    public Media(String type) {
        this.type = type;
    }

    public net.deechael.khl.message.cardmessage.module.Media setSrc(String src) {
        this.src = src;
        return this;
    }

    public String getSrc() {
        return src;
    }

    public net.deechael.khl.message.cardmessage.module.Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("src", src);
        json.addProperty("title", title);
        return json;
    }

}
