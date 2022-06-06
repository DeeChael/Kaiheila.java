package net.deechael.khl.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class TimeUtil {

    public static LocalDateTime convertUnixTimeMillisecondLocalDateTime(long mUnixTime) {
        long s = mUnixTime / 1000;
        int c = (int) (mUnixTime - s * 1000);
        return LocalDateTime.ofEpochSecond(s, c, OffsetDateTime.now().getOffset());
    }

}
