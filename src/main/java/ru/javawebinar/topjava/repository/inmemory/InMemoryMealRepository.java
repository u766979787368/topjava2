package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        if (meal.getUserId() != userId) return null;
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) throws NullPointerException {
        if (get(id, userId) == null) return false;
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) throws NullPointerException {
        if (repository.get(id).getUserId() != userId) return null;
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted((m1, m2) -> DateTimeUtil.compareDate(m1.getDateTime(), m2.getDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MealTo> getFilteredMeals(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, int userId) {
        return MealsUtil.getTos(getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpenDate(meal.getDateTime().toLocalDate(), fromDate, toDate))
                .filter(meal -> DateTimeUtil.isBetweenHalfOpenTime(meal.getDateTime().toLocalTime(), fromTime, toTime))
                .collect(Collectors.toList());
    }
}

