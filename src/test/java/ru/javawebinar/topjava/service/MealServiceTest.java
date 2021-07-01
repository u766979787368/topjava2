package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100007, USER_ID);
        assertThat(meal).usingRecursiveComparison().isEqualTo(meal100007);
    }

    @Test
    public void delete() {
        service.delete(100007, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(100003, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        service.create(new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 1, 12, 30), "Coca", 500), USER_ID);
        List<Meal> all = service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 31),
                LocalDate.of(2020, Month.JANUARY, 31),
                USER_ID);
        assertThat(all).usingDefaultElementComparator().isEqualTo(userMeals);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(
                                LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                                "Coca",
                                500),
                        USER_ID));

    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertThat(all).usingDefaultElementComparator().isEqualTo(userMeals);
    }

    @Test
    public void update() {
        Meal updatedMeal = new Meal(meal100007);
        updatedMeal.setDescription("newОбед");
        updatedMeal.setCalories(1001);
        updatedMeal.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 30));
        service.update(updatedMeal, USER_ID);
        assertThat(service.get(100007, USER_ID)).usingRecursiveComparison().isEqualTo(updatedMeal);
    }

    @Test
    public void create() {
        Meal created = service.create(new Meal(LocalDateTime.of(2021, Month.JULY, 1, 12, 30), "Coca", 500), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = new Meal(LocalDateTime.of(2021, Month.JULY, 1, 12, 30), "Coca", 500);
        newMeal.setId(newId);
        assertThat(created).usingRecursiveComparison().isEqualTo(newMeal);
        assertThat(service.get(newId, USER_ID)).usingRecursiveComparison().isEqualTo(newMeal);

    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(100003, USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(100003, USER_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updatedMeal = new Meal(meal100003);
        updatedMeal.setDescription("newОбед");
        updatedMeal.setCalories(1001);
        updatedMeal.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 30));
        assertThrows(NotFoundException.class, () -> service.update(updatedMeal, USER_ID));
    }

}