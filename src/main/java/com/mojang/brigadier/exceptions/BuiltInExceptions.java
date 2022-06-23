// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT license.

package com.mojang.brigadier.exceptions;

import com.mojang.brigadier.LiteralMessage;

public class BuiltInExceptions implements BuiltInExceptionProvider {
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> new LiteralMessage("双精度浮点数的值不能小于 " + min + "，发现了 " + found));
    private static final Dynamic2CommandExceptionType DOUBLE_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> new LiteralMessage("双精度浮点数的值不能大于 " + max + "，发现了 " + found));

    private static final Dynamic2CommandExceptionType FLOAT_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> new LiteralMessage("单精度浮点数的值不能小于 " + min + "，发现了 " + found));
    private static final Dynamic2CommandExceptionType FLOAT_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> new LiteralMessage("单精度浮点数的值不能大于 " + max + "，发现了 " + found));

    private static final Dynamic2CommandExceptionType INTEGER_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> new LiteralMessage("整数的值不能小于 " + min + "，发现了 " + found));
    private static final Dynamic2CommandExceptionType INTEGER_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> new LiteralMessage("整数的值不能大于 " + max + "，发现了 " + found));

    private static final Dynamic2CommandExceptionType LONG_TOO_SMALL = new Dynamic2CommandExceptionType((found, min) -> new LiteralMessage("长整数的值不能小于 " + min + "，发现了 " + found));
    private static final Dynamic2CommandExceptionType LONG_TOO_BIG = new Dynamic2CommandExceptionType((found, max) -> new LiteralMessage("长整数的值不能大于 " + max + "，发现了 " + found));

    private static final DynamicCommandExceptionType LITERAL_INCORRECT = new DynamicCommandExceptionType(expected -> new LiteralMessage("需要的文字 " + expected));

    private static final SimpleCommandExceptionType READER_EXPECTED_START_OF_QUOTE = new SimpleCommandExceptionType(new LiteralMessage("需要的字符串以引号开头"));
    private static final SimpleCommandExceptionType READER_EXPECTED_END_OF_QUOTE = new SimpleCommandExceptionType(new LiteralMessage("未封闭的引用字符串"));
    private static final DynamicCommandExceptionType READER_INVALID_ESCAPE = new DynamicCommandExceptionType(character -> new LiteralMessage("无效转义序列 '" + character + "' 在引用字符串中"));
    private static final DynamicCommandExceptionType READER_INVALID_BOOL = new DynamicCommandExceptionType(value -> new LiteralMessage("无效的布尔值，预期为true或false但是发现了 '" + value + "'"));
    private static final DynamicCommandExceptionType READER_INVALID_INT = new DynamicCommandExceptionType(value -> new LiteralMessage("无效的整数 '" + value + "'"));
    private static final SimpleCommandExceptionType READER_EXPECTED_INT = new SimpleCommandExceptionType(new LiteralMessage("需要整数"));
    private static final DynamicCommandExceptionType READER_INVALID_LONG = new DynamicCommandExceptionType(value -> new LiteralMessage("无效的长整数 '" + value + "'"));
    private static final SimpleCommandExceptionType READER_EXPECTED_LONG = new SimpleCommandExceptionType((new LiteralMessage("需要长整数")));
    private static final DynamicCommandExceptionType READER_INVALID_DOUBLE = new DynamicCommandExceptionType(value -> new LiteralMessage("无效的双精度浮点数 '" + value + "'"));
    private static final SimpleCommandExceptionType READER_EXPECTED_DOUBLE = new SimpleCommandExceptionType(new LiteralMessage("需要双精度浮点数"));
    private static final DynamicCommandExceptionType READER_INVALID_FLOAT = new DynamicCommandExceptionType(value -> new LiteralMessage("无效的单精度浮点数 '" + value + "'"));
    private static final SimpleCommandExceptionType READER_EXPECTED_FLOAT = new SimpleCommandExceptionType(new LiteralMessage("需要单精度浮点数"));
    private static final SimpleCommandExceptionType READER_EXPECTED_BOOL = new SimpleCommandExceptionType(new LiteralMessage("需要布尔值"));
    private static final DynamicCommandExceptionType READER_EXPECTED_SYMBOL = new DynamicCommandExceptionType(symbol -> new LiteralMessage("需要 '" + symbol + "'"));

    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_COMMAND = new SimpleCommandExceptionType(new LiteralMessage("未知的命令"));
    private static final SimpleCommandExceptionType DISPATCHER_UNKNOWN_ARGUMENT = new SimpleCommandExceptionType(new LiteralMessage("命令参数不正确"));
    private static final SimpleCommandExceptionType DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR = new SimpleCommandExceptionType(new LiteralMessage("需要空格来结束一个参数，但发现有数据跟随其后"));
    private static final DynamicCommandExceptionType DISPATCHER_PARSE_EXCEPTION = new DynamicCommandExceptionType(message -> new LiteralMessage("无法解析命令：" + message));

    @Override
    public Dynamic2CommandExceptionType doubleTooLow() {
        return DOUBLE_TOO_SMALL;
    }

    @Override
    public Dynamic2CommandExceptionType doubleTooHigh() {
        return DOUBLE_TOO_BIG;
    }

    @Override
    public Dynamic2CommandExceptionType floatTooLow() {
        return FLOAT_TOO_SMALL;
    }

    @Override
    public Dynamic2CommandExceptionType floatTooHigh() {
        return FLOAT_TOO_BIG;
    }

    @Override
    public Dynamic2CommandExceptionType integerTooLow() {
        return INTEGER_TOO_SMALL;
    }

    @Override
    public Dynamic2CommandExceptionType integerTooHigh() {
        return INTEGER_TOO_BIG;
    }

    @Override
    public Dynamic2CommandExceptionType longTooLow() {
        return LONG_TOO_SMALL;
    }

    @Override
    public Dynamic2CommandExceptionType longTooHigh() {
        return LONG_TOO_BIG;
    }

    @Override
    public DynamicCommandExceptionType literalIncorrect() {
        return LITERAL_INCORRECT;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedStartOfQuote() {
        return READER_EXPECTED_START_OF_QUOTE;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedEndOfQuote() {
        return READER_EXPECTED_END_OF_QUOTE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidEscape() {
        return READER_INVALID_ESCAPE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidBool() {
        return READER_INVALID_BOOL;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidInt() {
        return READER_INVALID_INT;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedInt() {
        return READER_EXPECTED_INT;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidLong() {
        return READER_INVALID_LONG;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedLong() {
        return READER_EXPECTED_LONG;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidDouble() {
        return READER_INVALID_DOUBLE;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedDouble() {
        return READER_EXPECTED_DOUBLE;
    }

    @Override
    public DynamicCommandExceptionType readerInvalidFloat() {
        return READER_INVALID_FLOAT;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedFloat() {
        return READER_EXPECTED_FLOAT;
    }

    @Override
    public SimpleCommandExceptionType readerExpectedBool() {
        return READER_EXPECTED_BOOL;
    }

    @Override
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return READER_EXPECTED_SYMBOL;
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownCommand() {
        return DISPATCHER_UNKNOWN_COMMAND;
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownArgument() {
        return DISPATCHER_UNKNOWN_ARGUMENT;
    }

    @Override
    public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
        return DISPATCHER_EXPECTED_ARGUMENT_SEPARATOR;
    }

    @Override
    public DynamicCommandExceptionType dispatcherParseException() {
        return DISPATCHER_PARSE_EXCEPTION;
    }
}
