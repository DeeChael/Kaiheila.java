package net.deechael.khl.api;

public interface TextChannel extends Channel {

    void updateSlowMode(SlowMode slowMode);

    void updateTopic(String topic);

}
