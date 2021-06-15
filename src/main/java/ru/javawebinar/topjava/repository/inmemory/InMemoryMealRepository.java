package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public synchronized Meal save(Meal meal, int currentUserId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        Meal foundMeal = repository.get(meal.getId());
        if (foundMeal != null && foundMeal.getUserId() != currentUserId) return null;

        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public synchronized boolean delete(int id, int currentUserId) {
        Meal foundMeal = repository.get(id);

        if (foundMeal == null) return false;

        return (foundMeal.getUserId() == currentUserId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int currentUserId) {
        Meal foundMeal = repository.get(id);

        if (foundMeal == null) return null;
        if (foundMeal.getUserId() != currentUserId) return null;

        return foundMeal;
    }

    @Override
    public Collection<Meal> getAll(int currentUserId) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == currentUserId)
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Meal> getBetweenDateBoundaries(LocalDate startDate, LocalDate endDate, int currentUserId) {
        return repository.values()
                .stream()
                .filter(meal -> meal.getUserId() == currentUserId && DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .collect(Collectors.toList());
    }
}

