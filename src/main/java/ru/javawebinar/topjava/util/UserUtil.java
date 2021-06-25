package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class UserUtil {

    public static final List<User> users = Arrays.asList(
            new User(1, "Ivan" , "Ivan@mail.ru", "password", Role.ADMIN),
            new User(2, "Bobby" , "Jone@mail.ru", "password", Role.USER)
    );


}
