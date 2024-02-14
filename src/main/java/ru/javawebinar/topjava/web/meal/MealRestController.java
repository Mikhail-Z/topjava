package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public List<ru.javawebinar.topjava.to.MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(
                service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public MealTo get(int id) {
        int userId = SecurityUtil.authUserId();
        List<Meal> allMeals = service.getAll(userId);

        return MealsUtil.filterByPredicate(
                    allMeals,
                    MealsUtil.DEFAULT_CALORIES_PER_DAY,
                    meal -> meal.getId() == id)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public MealTo create(MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.create(mealTo, userId);
        Meal createdMeal = service.create(meal, userId);

        return get(createdMeal.getId());
    }

    public MealTo update(MealTo mealTo) {
        int userId = SecurityUtil.authUserId();
        Meal meal = MealsUtil.create(mealTo, userId);
        Meal createdMeal = service.create(meal, userId);

        return get(createdMeal.getId());
    }

    public List<ru.javawebinar.topjava.to.MealTo> getBetweenTimeBoundaries(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getFilteredTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }
}