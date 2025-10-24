package org.scribe.services;

import java.util.Random;

/* loaded from: classes.dex */
public class TimestampServiceImpl implements TimestampService {
    private Timer timer = new Timer();

    @Override // org.scribe.services.TimestampService
    public String getNonce() {
        Long ts = getTs();
        return String.valueOf(ts.longValue() + this.timer.getRandomInteger().intValue());
    }

    @Override // org.scribe.services.TimestampService
    public String getTimestampInSeconds() {
        return String.valueOf(getTs());
    }

    private Long getTs() {
        return Long.valueOf(this.timer.getMilis().longValue() / 1000);
    }

    static class Timer {
        private final Random rand = new Random();

        Timer() {
        }

        Long getMilis() {
            return Long.valueOf(System.currentTimeMillis());
        }

        Integer getRandomInteger() {
            return Integer.valueOf(this.rand.nextInt());
        }
    }
}
