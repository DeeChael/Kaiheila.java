package net.deechael.khl.api;

import net.deechael.khl.type.ChannelTypes;

public interface Category extends Channel {

    Channel createChannel(ChannelTypes type, String name);

    TextChannel createTextChannel(String name);

    VoiceChannel createVoiceChannel(String name, int limit, VoiceChannel.Quality quality);

}
