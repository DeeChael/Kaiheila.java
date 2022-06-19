package net.deechael.khl.core.action;

public enum InviteDuration {
  HALF_HOUR(1800),
  ONE_HOUR(3600),
  SIX_HOURS(21600),
  TWELVE_HOURS(43200),
  ONE_DAY(86400),
  ONE_WEEK(604800),
  NERVER(0);

  public final int value;

  InviteDuration(int value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
