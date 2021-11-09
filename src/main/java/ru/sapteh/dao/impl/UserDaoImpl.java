package ru.sapteh.dao.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.sapteh.dao.Dao;
import ru.sapteh.model.User;

import java.util.List;


public class UserDaoImpl implements Dao<User, Integer> {

    private final SessionFactory factory;

    public UserDaoImpl(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public User findById(Integer id) {
        try(Session session = factory.openSession()) {
            return session.get(User.class, id);
        }
    }

    @Override
    public List<User> findAll() {
        try(Session session = factory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public boolean save(User user) {
        if (user == null) return false;
        try(Session session = factory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        }
        return true;
    }

    @Override
    public boolean update(User user) {
        if (user == null) return false;
        try(Session session = factory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        if (user == null) return false;
        try(Session session = factory.openSession()) {
            final Transaction transaction = session.beginTransaction();
            session.delete(user);
            transaction.commit();
        }
        return true;
    }
}