package com.javarush.repository;

import com.javarush.entity.City;
import com.javarush.entity.Country;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class CityRepository {

    private final SessionFactory sessionFactory;
    private final CountryRepository countryRepository;

    public CityRepository(SessionFactory sessionFactory, CountryRepository countryRepository) {
        this.sessionFactory = sessionFactory;
        this.countryRepository = countryRepository;
    }

    public City getById(Integer id) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from City c join fetch c.country where c.id = :ID", City.class);
        query.setParameter("ID", id);
        return query.getSingleResult();
    }

    public List<City> getItems(int offset, int limit) {
        Query<City> query = sessionFactory.getCurrentSession().createQuery("select c from City c", City.class);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.list();
    }

    public int getTotalCount() {
        Query<Long> query = sessionFactory.getCurrentSession().createQuery("select count(c) from City c", Long.class);
        return Math.toIntExact(query.uniqueResult());
    }

    public List<City> fetchData() {
        try (Session session = sessionFactory.getCurrentSession()) {
            List<City> allCities = new ArrayList<>();

            session.beginTransaction();
            List<Country> countries = countryRepository.getAll();

            int totalCount = getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }
}
