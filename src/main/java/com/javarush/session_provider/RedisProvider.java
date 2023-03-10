package com.javarush.session_provider;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;

public class RedisProvider {

    private final String HOST = "localhost";
    private final int PORT = 6379;

    public RedisClient prepareRedisClient() {
        return RedisClient.create(RedisURI.create(HOST, PORT));
    }

}
