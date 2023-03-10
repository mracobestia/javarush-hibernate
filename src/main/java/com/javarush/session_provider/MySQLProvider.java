package com.javarush.session_provider;

import com.javarush.entity.City;
import com.javarush.entity.Country;
import com.javarush.entity.CountryLanguage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class MySQLProvider implements SessionProvider {

    @Override
    public SessionFactory getSessionFactory() {
        Properties properties = new Properties();
        properties.setProperty(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        properties.setProperty(AvailableSettings.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
        properties.setProperty(AvailableSettings.URL, "jdbc:p6spy:mysql://localhost:3306/world");
        properties.setProperty(AvailableSettings.USER, "root");
        properties.setProperty(AvailableSettings.PASS, "root");
        properties.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(AvailableSettings.HBM2DDL_AUTO, "validate");
        properties.setProperty(AvailableSettings.STATEMENT_BATCH_SIZE, "100");

        return new Configuration()
                .addProperties(properties)
                .addAnnotatedClass(Country.class)
                .addAnnotatedClass(City.class)
                .addAnnotatedClass(CountryLanguage.class)
                .buildSessionFactory();
    }
}
