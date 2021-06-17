package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Dao implements CRUD {

    private AtomicInteger id = new AtomicInteger(0);

    private List<Meal> meals;

    {
        meals = new CopyOnWriteArrayList<>();
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of( 2020, 1, 30, 10, 0), "завтрак", 500));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 30, 13, 0), "обед", 1000));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 30, 20, 0), "ужин", 500));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 31, 0, 0), "на границе", 100));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 31, 10, 0), "завтрак", 1000));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 31, 13, 0), "обед", 500));
        meals.add(new Meal(id.incrementAndGet(),LocalDateTime.of(2020, 1, 31, 20, 0), "ужин", 410));
    }

    @Override
    public List<Meal> getAll() {
        return meals;
    }

    @Override
    public void delete(int id) {
        Optional<Meal> first = meals.stream().filter(meal -> meal.getId() == id).findFirst();
        meals.remove(first.get());
    }

    @Override
    public Meal getMeal(int id) {
        Optional<Meal> first = meals.stream().filter(meal -> meal.getId() == id).findFirst();
        return meals.get(meals.indexOf(first.get()));
    }


    @Override
    public void createOrUpdate(Meal newMeal) {
        boolean isNew = true;
        for (Meal meal : meals) {
            if (meal.getId() == newMeal.getId()) {
                meal.setDateTime(newMeal.getDateTime());
                meal.setCalories(newMeal.getCalories());
                meal.setDescription(newMeal.getDescription());
                isNew = false;
                break;
            }
        }
        if (isNew) {
            newMeal.setId(id.incrementAndGet());
            meals.add(newMeal);
        }
    }


}
