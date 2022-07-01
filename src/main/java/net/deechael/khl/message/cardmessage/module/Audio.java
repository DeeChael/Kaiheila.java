package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Audio extends Media {

    private String cover;

    public Audio() {
        super("audio");
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("cover", cover);
        return json;
    }
}
