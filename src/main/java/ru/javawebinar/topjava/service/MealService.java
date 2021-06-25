package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Service
public class MealService {

    private final MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal save(Meal meal, int userId) {
        Meal meal1 = repository.save(meal, userId);;
        if (meal1 == null) throw new NotFoundException("Такой mealId не найден или не принадлежит тебе");
        return meal1;
    }

    // false if meal do not belong to userId
    public void delete(int id, int userId){
        try {
            if (!repository.delete(id, userId)) {
                throw new NotFoundException("Такой mealId не найден или не принадлежит тебе");
            }
            ;
        } catch (NullPointerException e) {
            throw new NotFoundException("Такой mealId не найден или не принадлежит тебе");
        }
    }

    // null if meal do not belong to userId
    public Meal get(int id, int userId){
        Meal meal;
        try {
            meal = repository.get(id, userId);
        } catch (NullPointerException e) {
            throw new NotFoundException("Такой mealId не найден или не принадлежит тебе");
        }
        if (meal == null) throw new NotFoundException("Такой mealId не найден или не принадлежит тебе");
        return meal;
    }

    // ORDERED dateTime desc
    public Collection<Meal> getAll(int userId){
        return repository.getAll(userId);
    }

    public Collection<MealTo> getFilteredMeals(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int userId) {
        return repository.getFilteredMeals(fromDate, toDate, fromTime, toTime, userId);
    }
}