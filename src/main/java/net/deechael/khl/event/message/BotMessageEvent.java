package net.deechael.khl.event.message;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.event.AbstractEvent;
import net.deechael.khl.event.IEvent;
import com.fasterxml.jackson.databind.JsonNode;

public class BotMessageEvent extends AbstractEvent {

  public BotMessageEvent(KaiheilaBot rabbit, JsonNode node) {
    super(rabbit, node);
  }

    @Override
  public IEvent handleSystemEvent(JsonNode body) {
    return this;
  }
}
