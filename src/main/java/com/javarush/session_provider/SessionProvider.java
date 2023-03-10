package com.javarush.session_provider;

import org.hibernate.SessionFactory;

public interface SessionProvider {

    SessionFactory getSessionFactory();

}
