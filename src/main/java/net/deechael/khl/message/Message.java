package net.deechael.khl.message;

public interface Message {

    String getContent();

    String asString();

    MessageTypes getType();

}
