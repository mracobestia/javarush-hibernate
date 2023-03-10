package com.javarush.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.entity.City;
import com.javarush.redis.CityCountry;
import com.javarush.redis.CityMapper;
import com.javarush.repository.CityRepository;
import com.javarush.repository.CountryRepository;
import com.javarush.session_provider.RedisProvider;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStringCommands;
import lombok.Getter;
import org.hibernate.SessionFactory;

import java.util.List;

@Getter
public class Service {

    private final SessionFactory sessionFactory;
    private final CityRepository cityRepository;
    private final RedisClient redisClient;

    private final ObjectMapper mapper;

    public Service(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.cityRepository = new CityRepository(sessionFactory, new CountryRepository(sessionFactory));
        this.redisClient = new RedisProvider().prepareRedisClient();
        this.mapper = new ObjectMapper();
    }

    public void pushCityDataToRedis() {
        List<City> allCities = cityRepository.fetchData();

        List<CityCountry> preparedData = transformData(allCities);
        pushToRedis(preparedData);

        sessionFactory.getCurrentSession().close();
    }

    private List<CityCountry> transformData(List<City> cities) {

        CityMapper cityMapper = CityMapper.INSTANCE;
        return cities.stream()
                .map(cityMapper::toCityCountry)
                .toList();
    }

    private void pushToRedis(List<CityCountry> data) {
        try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
            RedisStringCommands<String, String> sync = connection.sync();
            for (CityCountry cityCountry : data) {
                try {
                    sync.set(String.valueOf(cityCountry.getId()), mapper.writeValueAsString(cityCountry));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
