package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Countdown extends Module {
    private static final String type = "countdown";
    private int startTime;
    private int endTime;
    private Mode mode;

    public net.deechael.khl.message.cardmessage.module.Countdown setStartTime(int startTime) {
        this.startTime = startTime;
        return this;
    }

    public net.deechael.khl.message.cardmessage.module.Countdown setEndTime(int endTime) {
        this.endTime = endTime;
        return this;
    }

    public net.deechael.khl.message.cardmessage.module.Countdown setMode(Mode mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("startTime", startTime);
        json.addProperty("endTime", endTime);
        json.addProperty("mode", mode.value);
        return json;
    }

    public enum Mode {
        DAY,
        HOUR,
        SECOND;

        private final String value;

        Mode() {
            this.value = this.name().toLowerCase();
        }

        public String getValue() {
            return value;
        }

    }
}
