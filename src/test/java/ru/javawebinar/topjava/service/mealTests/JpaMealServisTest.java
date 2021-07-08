package ru.javawebinar.topjava.service.mealTests;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.JPA)
public class JpaMealServisTest extends MealServiceTest {
}
