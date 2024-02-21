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
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.GUEST_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.MealTestData.*;

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
        int userId = USER_ID;
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, userId);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, userId), newMeal);
    }

    @Test
    public void duplicateMailCreate() {
        LocalDateTime duplicateDateTime = savedUserMeal().getDateTime();
        int duplicateUserId = USER_ID;
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(duplicateDateTime, "new description", 100), duplicateUserId));
    }

    @Test
    public void delete() {
        int deletedMealId = savedUserMeal().getId();
        service.delete(deletedMealId, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(deletedMealId, USER_ID));
    }

    @Test
    public void delete_whenInvalidUser() {
        int deletedMealId = savedUserMeal().getId();
        assertThrows(NotFoundException.class, () -> service.delete(deletedMealId, UserTestData.GUEST_ID));
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
    public void get_whenInvalidUser() {
        assertThrows(NotFoundException.class, () -> service.get(savedUserMeal().getId(), GUEST_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDt = LocalDate.of(2020, 1, 31);
        LocalDate endDt = LocalDate.of(2020, 1, 31);
        List<Meal> meals = service.getBetweenInclusive(startDt, endDt, USER_ID);
        assertMatch(meals, MEAL_7, MEAL_6, MEAL_5, MEAL_4);
    }

    @Test
    public void getBetweenInclusive_whenNoMealsByUser() {
        LocalDate startDt = LocalDate.of(2020, 1, 31);
        LocalDate endDt = LocalDate.of(2020, 1, 31);
        List<Meal> meals = service.getBetweenInclusive(startDt, endDt, GUEST_ID);
        assertMatch(Collections.emptyList(), meals);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, MEAL_7, MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
    }

    @Test
    public void getAll_whenNoMealsByUser() {
        List<Meal> meals = service.getAll(GUEST_ID);
        assertMatch(Collections.emptyList(), meals);
    }

    private Meal savedUserMeal() {
        return MealTestData.USER_MEALS.get(0);
    }
}
