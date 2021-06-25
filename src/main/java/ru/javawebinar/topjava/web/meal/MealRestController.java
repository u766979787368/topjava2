package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll () {
        return MealsUtil.getTos(service.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        return service.get(id, authUserId());
    }

    public Meal save(Meal meal) {
        return service.save(meal, authUserId());
    }

    public List<MealTo> getFilteredMeals (LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        return new ArrayList<>(service.getFilteredMeals(fromDate, toDate, fromTime, toTime, authUserId()));
    }



}