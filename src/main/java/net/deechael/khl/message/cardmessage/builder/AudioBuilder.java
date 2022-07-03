package net.deechael.khl.message.cardmessage.builder;

import net.deechael.khl.message.cardmessage.module.Audio;

public class AudioBuilder extends MediaBuilder {

    private final Audio audio = new Audio();

    AudioBuilder(CardBuilder parent) {
        super(parent);
    }

    public AudioBuilder cover(String cover) {
        this.audio.setCover(cover);
        return this;
    }

    @Override
    Audio module() {
        return audio;
    }

    @Override
    Audio media() {
        return audio;
    }

}
