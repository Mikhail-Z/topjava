package ru.javawebinar.topjava.service.meal;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends MealServiceTest {
}
