package net.deechael.khl.core.action;

import com.fasterxml.jackson.databind.JsonNode;

public enum OperationResult {
  SUCCESS,
  FAILED;

  JsonNode result = null;
  Operation action;

  public Operation getOperation() {
    return action;
  }

  OperationResult() {}

  public JsonNode getResult() {
    return result;
  }

  public OperationResult setAction(Operation action) {
    this.action = action;
    return this;
  }

  public OperationResult setResult(JsonNode result) {
    this.result = result;
    return this;
  }
}
