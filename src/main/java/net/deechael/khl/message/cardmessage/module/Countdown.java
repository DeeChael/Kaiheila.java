package net.deechael.khl.message.cardmessage.module;

import com.google.gson.JsonObject;

public class Countdown extends Module {

    private long startTime = 0;
    private long endTime = 0;
    private Mode mode = Mode.DAY;

    public Countdown() {
        super("countdown");
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public JsonObject asJson() {
        JsonObject json = super.asJson();
        json.addProperty("startTime", startTime);
        json.addProperty("endTime", endTime);
        json.addProperty("mode", mode.toString());
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

        @Override
        public String toString() {
            return value;
        }

    }
}
