package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        List<UserMealWithExcess> resultMeals = new ArrayList<UserMealWithExcess>(meals.size());
        Map<LocalDate, Integer> caloriesPerDay = new HashMap<LocalDate, Integer>();

        for (UserMeal userMeal : meals) {
            caloriesPerDay.computeIfPresent(userMeal.getDateTime().toLocalDate(), (date, cnt) -> cnt + userMeal.getCalories());
            caloriesPerDay.putIfAbsent(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
        }
        for (UserMeal userMeal : meals) {
            boolean excess = caloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > maxCaloriesPerDay;
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                resultMeals.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess));
            }
        }

        return resultMeals;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int maxCaloriesPerDay) {
        Map<LocalDate, Integer> caloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(
                    (UserMeal meal) -> meal.getDateTime().toLocalDate(),
                    Collectors.summingInt(UserMeal::getCalories)));

        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    boolean excess = caloriesPerDay.get(userMeal.getDateTime().toLocalDate()) > maxCaloriesPerDay;
                    return new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), excess);
                })
                .collect(Collectors.toList());
    }
}
