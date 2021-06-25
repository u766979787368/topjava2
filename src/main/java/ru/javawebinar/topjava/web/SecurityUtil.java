package ru.javawebinar.topjava.web;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;

public class SecurityUtil {

    private static int user = 1;

    public static int authUserId() {
        return user;
    }

    public static void setUser(int user) {
        SecurityUtil.user = user;
    }

    public static int authUserCaloriesPerDay() {
        return DEFAULT_CALORIES_PER_DAY;
    }
}