package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.entity.City;
import com.javarush.entity.CountryLanguage;
import com.javarush.redis.CityCountry;
import com.javarush.repository.CityRepository;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

public class TestDataService {

    private final SessionFactory sessionFactory;
    private final RedisClient redisClient;
    private final CityRepository cityRepository;
    private final ObjectMapper mapper;

    public TestDataService(SessionFactory sessionFactory, RedisClient redisClient, CityRepository cityRepository) {
        this.sessionFactory = sessionFactory;
        this.redisClient = redisClient;
        this.cityRepository = cityRepository;
        this.mapper = new ObjectMapper();
    }

    public void testRedisData(List<Integer> ids) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (Integer id : ids) {
                String value = sync.get(String.valueOf(id));
                try {
                    mapper.readValue(value, CityCountry.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void testMysqlData(List<Integer> ids) {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            for (Integer id : ids) {
                City city = cityRepository.getById(id);
                Set<CountryLanguage> languages = city.getCountry().getLanguages();
            }
            session.getTransaction().commit();
        }
    }

    public void shutdown() {
        if (nonNull(sessionFactory)) {
            sessionFactory.close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }

    public List<Integer> getRandomCityId() {
        try (Session session = sessionFactory.getCurrentSession()) {
            session.beginTransaction();

            List<Integer> list = session
                    .createNamedQuery(City.RANDOM_20_CITY_ID_QUERY, Integer.class)
                    .list();

            session.getTransaction().commit();

            return list;
        }
    }

}
