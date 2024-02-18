package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealTo > getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll");
        return MealsUtil.getTos(
                service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("creating {}", meal);
        Meal createdMeal = service.create(meal, userId);
        log.info("created {}", createdMeal);

        return createdMeal;
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, id);
        log.info("updating {}", meal);
        service.update(meal, userId);
        log.info("updated {}", meal);
    }

    public List<MealTo> getBetweenTimeBoundaries(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();

        LocalDate resStartDate = startDate == null ? LocalDate.MIN : startDate;
        LocalTime resStartTime = startTime == null ? LocalTime.MIN : startTime;
        LocalDate resEndDate = endDate == null ? LocalDate.MIN : endDate;
        LocalTime resEndTime = endTime == null ? LocalTime.MIN : endTime;

        List<Meal> allMeals = service.getAll(userId);
        return MealsUtil.filterByPredicate(
                allMeals,
                SecurityUtil.authUserCaloriesPerDay(),
                meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), resStartTime, resEndTime) && DateTimeUtil.isBetween(meal.getDate(), resStartDate, resEndDate));
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }
}