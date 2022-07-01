package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Media extends Module {

    private String src;
    private String title;

    public Media(String type) {
        super(type);
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("src", src);
        json.addProperty("title", title);
        return json;
    }

}
