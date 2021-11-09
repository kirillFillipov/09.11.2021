package ru.sapteh.service;

import org.hibernate.SessionFactory;
import ru.sapteh.dao.impl.AdminDaoImpl;

import java.util.List;

public class AdminService {

    private final AdminDaoImpl adminDao;

    public AdminService(SessionFactory factory) {
        this.adminDao = new AdminDaoImpl(factory);
    }

    public ru.sapteh.model.Admin findById(int id) {
        return adminDao.findById(id);
    }

    public List<ru.sapteh.model.Admin> findAll() {
        return adminDao.findAll();
    }

    public boolean save(ru.sapteh.model.Admin admin) {
        return adminDao.save(admin);
    }

    public void update(ru.sapteh.model.Admin admin) {
        adminDao.update(admin);
    }

    public void delete(ru.sapteh.model.Admin admin) {
        adminDao.delete(admin);
    }

    private class Admin {
    }
}