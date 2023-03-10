package com.javarush;

import com.javarush.service.Service;
import com.javarush.service.TestDataService;
import com.javarush.session_provider.MySQLProvider;
import com.javarush.session_provider.SessionProvider;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        SessionProvider sessionProvider = new MySQLProvider();
        Service service = new Service(sessionProvider.getSessionFactory());
        service.pushCityDataToRedis();

        TestDataService testDataService = new TestDataService(
                service.getSessionFactory(),
                service.getRedisClient(),
                service.getCityRepository());

        List<Integer> ids = testDataService.getRandomCityId();

        Instant start = Instant.now();
        testDataService.testRedisData(ids);
        Instant finish = Instant.now();
        long elapsedRedis = Duration.between(start, finish).toMillis();

        start = Instant.now();
        testDataService.testMysqlData(ids);
        finish = Instant.now();
        long elapsedMySQL = Duration.between(start, finish).toMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", elapsedRedis);
        System.out.printf("%s:\t%d ms\n", "MySQL", elapsedMySQL);

        testDataService.shutdown();

    }
}
