package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.UsersUtil.ADMIN_ID;
import static ru.javawebinar.topjava.util.UsersUtil.USER_ID;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> mealsByUserMap = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, USER_ID));
        save(new Meal(LocalDateTime.of(2021, Month.MAY, 20, 10, 0), "Первый прием пищи админа", 600), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2021, Month.MAY, 20, 14, 0), "Второй прием пищи админа", 1000), ADMIN_ID);
        save(new Meal(LocalDateTime.of(2021, Month.MAY, 20, 20, 0), "Третий прием пищи админа", 800), ADMIN_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if (mealsByUserMap.containsKey(userId)) {
                Map<Integer, Meal> userMeals = mealsByUserMap.get(userId);
                userMeals.put(meal.getId(), meal);
            } else {
                ConcurrentMap<Integer, Meal> userMeals = new ConcurrentHashMap<>();
                userMeals.put(meal.getId(), meal);
                mealsByUserMap.put(userId, userMeals);
            }
            return meal;
        }

        if (!mealsByUserMap.containsKey(userId)) {
            return null;
        }

        Map<Integer, Meal> userMeals = mealsByUserMap.get(userId);
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (!mealsByUserMap.containsKey(userId)) {
                return false;
        }

        Map<Integer, Meal> userMeals = mealsByUserMap.get(userId);
        return userMeals.get(id)!= null && userMeals.get(id) != null && userMeals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        if (!mealsByUserMap.containsKey(userId)) {
            return null;
        }

        Map<Integer, Meal> userMeals = mealsByUserMap.get(userId);
        return userMeals == null ? null : userMeals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFiltered(userId, meal -> true);
    }

    @Override
    public List<Meal> getBetweenDateBoundaries(LocalDate startDate, LocalDate endDate, int userId) {
        return getFiltered(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate));
    }

    private List<Meal> getFiltered(int userId, Predicate<Meal> predicate) {
        if (!mealsByUserMap.containsKey(userId)) {
            return new ArrayList<>();
        }

        Map<Integer, Meal> userMeals = mealsByUserMap.get(userId);
        return userMeals.values()
                .stream()
                .filter(predicate)
                .sorted((meal1, meal2) -> meal2.getDateTime().compareTo(meal1.getDateTime()))
                .collect(Collectors.toList());
    }
}

