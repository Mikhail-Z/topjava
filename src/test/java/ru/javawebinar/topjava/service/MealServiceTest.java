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
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

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
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(NEW_MEAL_ID);
        assertMatch(created, newMeal);
        assertMatch(service.get(NEW_MEAL_ID, USER_ID), newMeal);
    }

    @Test
    public void createWhenMealIsNotUnique() {
        service.create(getNew(), USER_ID);
        assertThrows(DataAccessException.class, () ->
                service.create(getNew(), USER_ID));
    }

    @Test
    public void update() {
        Meal changedMeal = new Meal(meal1.getId(), meal1.getDateTime(), meal1.getDescription(), meal1.getCalories());
        changedMeal.setDescription("new description");
        service.update(changedMeal, USER_ID);
    }

    @Test
    public void updateWhenActionOnForeignMeal() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(meal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(meal1.getId(), USER_ID));
    }

    @Test
    public void deleteWhenMealOfAnotherUser() {
        int deletedMealId = savedUserMeal().getId();
        assertThrows(NotFoundException.class, () -> service.delete(deletedMealId, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(savedUserMeal().getId(), USER_ID);
        assertMatch(meal, savedUserMeal());
    }

    @Test
    public void getWhenMealOfAnotherUser() {
        assertThrows(NotFoundException.class, () -> service.get(savedUserMeal().getId(), ADMIN_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusiveWhenBothBoundaries() {
        LocalDate startDt = LocalDate.of(2020, 1, 31);
        LocalDate endDt = LocalDate.of(2020, 1, 31);
        List<Meal> meals = service.getBetweenInclusive(startDt, endDt, USER_ID);
        assertMatch(meals, meal7, meal6, meal5, meal4);
    }

    @Test
    public void getBetweenInclusiveWhenNoBoundaries() {
        List<Meal> meals = service.getBetweenInclusive(null, null, USER_ID);
        assertMatch(meals, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    private Meal savedUserMeal() {
        return MealTestData.USER_MEALS.get(0);
    }
}
