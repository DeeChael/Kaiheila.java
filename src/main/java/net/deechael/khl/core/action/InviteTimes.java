package net.deechael.khl.core.action;

public enum InviteTimes {
  ONE_TIME(1),
  FIVE_TIMES(5),
  TEN_TIMES(10),
  TWENTY_FIVE_TIMES(25),
  FIFTY_TIMES(50),
  HUNDRADE_TIMES(100),
  UNLIMITED(-1);

  public final int value;

  InviteTimes(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
