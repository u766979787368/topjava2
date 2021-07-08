package ru.javawebinar.topjava.service.userTests;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class DatajpaUserServisTest extends UserServiceTest {

}
