package ru.javawebinar.topjava.repository.jdbc;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryTest {
    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealRepository mealRepository;

    @Test
    public void save() {
        Meal newMeal = getNew();
        Meal created = mealRepository.save(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
    }

    @Test
    public void delete() {
        boolean deleted = mealRepository.delete(MEAL_1.getId(), USER_ID);
        CoreMatchers.equalTo(deleted).matches(true);
    }

    @Test
    public void delete_whenInvalidUser() {
        boolean deleted = mealRepository.delete(MEAL_1.getId(), GUEST_ID);
        CoreMatchers.equalTo(deleted).matches(false);
    }

    @Test
    public void get() {
        Meal meal = mealRepository.get(MEAL_1.getId(), USER_ID);
        assertMatch(meal, MEAL_1);
    }

    @Test
    public void get_whenInvalidUser() {
        Meal meal = mealRepository.get(MEAL_1.getId(), GUEST_ID);
        assertMatch(meal, null);
    }

    @Test
    public void getBetweenHalfOpen() {
        LocalDateTime startDt = LocalDateTime.of(2020, 1, 31, 10, 0, 0);
        LocalDateTime endDt = LocalDateTime.of(2020, 1, 31, 20, 0, 0);
        List<Meal> meals = mealRepository.getBetweenHalfOpen(startDt, endDt, USER_ID);
        assertMatch(meals, MEAL_6, MEAL_5);
    }

    @Test
    public void getBetweenHalfOpen_whenNoMealsByUser() {
        LocalDateTime startDt = LocalDateTime.of(2020, 1, 31, 10, 0, 0);
        LocalDateTime endDt = LocalDateTime.of(2020, 1, 31, 20, 0, 0);
        List<Meal> meals = mealRepository.getBetweenHalfOpen(startDt, endDt, GUEST_ID);
        assertMatch(meals, Collections.emptyList());
    }

    @Test
    public void getAll() {
        List<Meal> meals = mealRepository.getAll(USER_ID);
        assertMatch(meals, MEAL_7, MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll_whenNoMealsByUser() {
        LocalDateTime startDt = LocalDateTime.of(2020, 1, 31, 10, 0, 0);
        LocalDateTime endDt = LocalDateTime.of(2020, 1, 31, 20, 0, 0);
        List<Meal> meals = mealRepository.getBetweenHalfOpen(startDt, endDt, GUEST_ID);
        assertMatch(meals, Collections.emptyList());
    }
}
