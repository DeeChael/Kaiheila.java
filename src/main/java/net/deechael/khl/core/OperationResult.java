package net.deechael.khl.core;

import com.fasterxml.jackson.databind.JsonNode;

public class OperationResult {

    private final Result result;
    private final JsonNode response;

    private OperationResult(Result result, JsonNode response) {
        this.result = result;
        this.response = response;
    }

    public JsonNode getResponse() {
        return response;
    }

    public Result getResult() {
        return result;
    }

    public static OperationResult success(JsonNode response) {
        return new OperationResult(Result.SUCCESS, response);
    }

    public static OperationResult failed() {
        return new OperationResult(Result.FAILED, null);
    }

}
