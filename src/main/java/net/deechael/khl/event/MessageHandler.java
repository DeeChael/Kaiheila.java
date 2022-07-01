package net.deechael.khl.event;

import net.deechael.khl.message.ReceivedChannelMessage;

public abstract class MessageHandler {

    public void onText(ReceivedChannelMessage message) {

    }

    public void onImage(ReceivedChannelMessage message) {

    }

    public void onVideo(ReceivedChannelMessage message) {

    }

    public void onFile(ReceivedChannelMessage message) {

    }

    public void onAudio(ReceivedChannelMessage message) {

    }

    public void onKMarkdown(ReceivedChannelMessage message) {

    }

    public void onCardMessage(ReceivedChannelMessage message) {

    }

    public void onSystem(ReceivedChannelMessage message) {

    }

}
