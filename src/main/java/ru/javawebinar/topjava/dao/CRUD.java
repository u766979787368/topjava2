package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface CRUD {

    List<Meal> getAll ();
    void delete(int id);
    Meal getMeal(int id);
    void createOrUpdate(Meal meal);

}
