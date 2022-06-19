package net.deechael.khl.core.action;

import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Guild;
import net.deechael.khl.api.Role;
import net.deechael.khl.api.User;
import net.deechael.khl.client.http.HttpCall;
import net.deechael.khl.client.http.RequestBuilder;
import net.deechael.khl.entity.cardmessage.CardMessageBuilder;
import net.deechael.khl.entity.kmarkdown.KMarkdown;
import net.deechael.khl.event.message.TextMessageEvent;
import net.deechael.khl.restful.RestRoute;
import net.deechael.khl.restful.RestfulService;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class Operation extends RestfulService {
  public Operation(KaiheilaBot rabbit) {
    super(rabbit);
  }

  protected boolean handleResult(JsonNode res){
    return res != null && res.get("code").asInt() == 0;
  }

  public static class ChatOperation extends Operation {
    TextMessageEvent message;

    public ChatOperation(KaiheilaBot rabbit, TextMessageEvent message) {
      super(rabbit);
      this.message = message;
    }

    public OperationResult replyOnChannel(String content, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthor().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult replyOnPerson(String content, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthor().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthor().getId())
            .withData("nonce", "bot-message")
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult replyOnChannel(CardMessageBuilder content, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("type", 10)
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthor().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult replyOnPerson(CardMessageBuilder content, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthor().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthor().getId())
            .withData("nonce", "bot-message")
            .withData("type", 10)
            .withData("content", content)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult replyOnChannel(KMarkdown kMarkdown, boolean isTemp, boolean isReply) {
      Log.info("Reply on channel {}", message.getChannel().getName());
      if (message.type == TextMessageEvent.Type.Group) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
            .withData("channel_id", message.getChannel().getId())
            .withData("nonce", "bot-message")
            .withData("type", 9)
            .withData("content", kMarkdown)
            .withData("quote", isReply ? message.messageID : null)
            .withData("temp_target_id", isTemp ? message.getEventAuthor().getId() : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult replyOnPerson(KMarkdown kMarkdown, boolean isReply) {
      Log.info("Reply on person {}", message.getEventAuthor().getFullName());
      if (message.type == TextMessageEvent.Type.Person) {
        HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
            .withData("target_id", message.getEventAuthor().getId())
            .withData("nonce", "bot-message")
            .withData("type", 9)
            .withData("content", kMarkdown.value)
            .withData("quote", isReply ? message.messageID : null)
            .build();
        try{
          JsonNode data = getRestActionJsonResponse(req);
          if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
          else return OperationResult.FAILED;
        } catch (InterruptedException e) {
          e.printStackTrace();
          return OperationResult.FAILED;
        }
      }
      return OperationResult.FAILED;
    }

    public OperationResult reply(String content) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(content, false, false);
      }
      return replyOnPerson(content, false);
    }

    public OperationResult reply(CardMessageBuilder cardMessage) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(cardMessage, false, false);
      }
      return replyOnPerson(cardMessage, false);
    }

    public OperationResult reply(KMarkdown kMarkdown) {
      if (message.type == TextMessageEvent.Type.Group) {
        return replyOnChannel(kMarkdown, false, false);
      }
      return replyOnPerson(kMarkdown, false);
    }

    public UserOperation getUser() {
      return message.type == TextMessageEvent.Type.Group ? new UserOperation(getKaiheilaBot(), message.getEventAuthor(), message.getChannel()) : new UserOperation(getKaiheilaBot(), message.getEventAuthor());
    }

    public ChannelOperation getChannel() {
      if (message.type == TextMessageEvent.Type.Person) return null; //todo: throw exception
      return new ChannelOperation(getKaiheilaBot(), message.getChannel());
    }

    public ServerOperation getGuild() {
      if (message.type == TextMessageEvent.Type.Person) return null; //todo: throw exception
      return new ServerOperation(getKaiheilaBot(), message.getChannel().getGuild());
    }
  }

  public static class UserOperation extends Operation {
    protected final User user;
    public final boolean isPersonal;
    protected final Channel channel;

    public UserOperation(KaiheilaBot rabbit, User user) {
      super(rabbit);
      this.user = user;
      this.isPersonal = true;
      channel = null;
    }

    public UserOperation(KaiheilaBot rabbit, User user, Channel channel) {
      super(rabbit);
      this.user = user;
      this.isPersonal = false;
      this.channel = channel;
    }

    public OperationResult sendMessage(String message) {
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
          .withData("target_id", user.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult reply(String message, String msgId) {
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.DirectMessage.SEND_DIRECT_MESSAGE)
          .withData("target_id", user.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .withData("quote", msgId)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult updateIntimacy(int value) {
      RestRoute.CompiledRoute route = RestRoute.Intimacy.UPDATE_USER_INTIMACY.compile()
          .withQueryParam("user_id", user.getId())
          .withQueryParam("score", value);
      HttpCall req = HttpCall.createRequest(route.getMethod(), getCompleteUrl(route), this.defaultHeaders);
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public int getUserIntimacy(){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.Intimacy.INTIMACY_LIST)
          .withQuery("user_id", user.getId())
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("score").asInt();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return -1;
    }

    public ServerOperation getServer(){
      if (isPersonal) return null; //todo throw exception
      return new ServerOperation(getKaiheilaBot(), channel.getGuild());
    }

    public ChannelOperation getChannel(){
      if (!isPersonal) return null; //todo throw exception
      return new ChannelOperation(getKaiheilaBot(), channel);
    }
  }

  public static class ServerOperation extends Operation {
    private final Guild server;

    public ServerOperation(KaiheilaBot rabbit, Guild server) {
      super(rabbit);
      this.server = server;
    }

    public ChannelOperation getChannel(String id){
      return new ChannelOperation(getKaiheilaBot(), getKaiheilaBot().getCacheManager().getChannelCache().getElementById(id));
    }

    public UserOperation getMember(String id){
      return new UserOperation(getKaiheilaBot(), getKaiheilaBot().getCacheManager().getUserCache().getElementById(id));
    }

    public RoleOperation getMember(int id){
      return new RoleOperation(getKaiheilaBot(), server, getKaiheilaBot().getCacheManager().getRoleCache().getElementById(id));
    }

    public String createServerInvite(InviteDuration duration, InviteTimes times){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.Invite.CREATE_INVITE)
          .withData("guild_id", server.getId())
          .withData("duration", duration)
          .withData("setting_times", times)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("data").get("url").asText();
        }else{
          Log.error("Failed to create server invite! Reason: {}",data.get("message").asText());
        }
      } catch (InterruptedException e) {
        Log.error("Failed to create server invite! Reason: {}", e.getMessage());
        e.printStackTrace();
      }
      return null;
    }
  }

  public static class RoleOperation extends Operation {
    private final Role role;
    private final Guild guild;

    public RoleOperation(KaiheilaBot rabbit, Guild guild, Role role) {
      super(rabbit);
      this.role = role;
      this.guild = guild;
    }

    public OperationResult grantUser(String uid){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.GuildRole.GRANT_GUILD_ROLE)
          .withData("guild_id", guild.getId())
          .withData("user_id", uid)
          .withData("role_id", String.valueOf(role.getId()))
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult revokeUser(String uid){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.GuildRole.REVOKE_GUILD_ROLE)
          .withData("guild_id", guild.getId())
          .withData("user_id", uid)
          .withData("role_id", String.valueOf(role.getId()))
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }
  }

  public static class ChannelOperation extends Operation {
    final Channel channel;

    public ChannelOperation(KaiheilaBot rabbit, Channel channel) {
      super(rabbit);
      this.channel = channel;
    }

    public OperationResult broadcastMessage(String message, boolean kMarkdown){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .withData("type", kMarkdown ? 9 : 1)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult sendTempMessage(String message, String uid, boolean kMarkdown){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("content", message)
          .withData("type", kMarkdown ? 9 : 1)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult broadcastMessage(CardMessageBuilder message) {
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 10)
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult sendTempMessage(CardMessageBuilder message, String uid){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 10)
          .withData("content", message)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult broadcastMessage(KMarkdown message) {
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 9)
          .withData("content", message)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public OperationResult sendTempMessage(KMarkdown message, String uid){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.ChannelMessage.SEND_CHANNEL_MESSAGE)
          .withData("target_id", channel.getId())
          .withData("nonce", "bot-message")
          .withData("type", 9)
          .withData("content", message)
          .withData("temp_target_id", uid)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) return OperationResult.SUCCESS.setAction(this).setResult(data.get("data"));
        else return OperationResult.FAILED;
      } catch (InterruptedException e) {
        e.printStackTrace();
        return OperationResult.FAILED;
      }
    }

    public String createChannelInvite(InviteDuration duration, InviteTimes times){
      HttpCall req = RequestBuilder.create(getKaiheilaBot(), RestRoute.Invite.CREATE_INVITE)
          .withData("channel_id", channel.getId())
          .withData("duration", duration)
          .withData("setting_times", times)
          .build();
      try{
        JsonNode data = getRestActionJsonResponse(req);
        if (handleResult(data)) {
          return data.get("url").asText();
        }else{
          Log.error("Failed to create server invite! Reason: {}",data.get("message").asText());
        }
      } catch (InterruptedException e) {
        Log.error("Failed to create server invite! Reason: {}", e.getMessage());
        e.printStackTrace();
      }
      return null;
    }

    public ServerOperation getServer(){
      return new ServerOperation(getKaiheilaBot(), channel.getGuild());
    }
  }
}
