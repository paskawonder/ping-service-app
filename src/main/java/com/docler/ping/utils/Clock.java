package com.docler.ping.utils;

import javax.inject.Singleton;

@Singleton
public class Clock {

    public long timestamp() {
        return System.currentTimeMillis();
    }

}
