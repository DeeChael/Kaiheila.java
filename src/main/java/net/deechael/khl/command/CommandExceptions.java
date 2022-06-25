package net.deechael.khl.command;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class CommandExceptions {

    public static final SimpleCommandExceptionType NOT_A_USER = new SimpleCommandExceptionType(new LiteralMessage("不是一个用户"));

    public static final SimpleCommandExceptionType USER_CANNOT_BE_FOUND = new SimpleCommandExceptionType(new LiteralMessage("获取用户失败"));

    public static final SimpleCommandExceptionType NOT_A_CHANNEL = new SimpleCommandExceptionType(new LiteralMessage("不是一个频道"));

    public static final SimpleCommandExceptionType CHANNEL_CANNOT_BE_FOUND = new SimpleCommandExceptionType(new LiteralMessage("获取频道失败"));

}
