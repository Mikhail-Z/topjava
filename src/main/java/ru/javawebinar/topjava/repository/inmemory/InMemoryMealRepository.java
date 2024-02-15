package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public synchronized Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        Meal foundMeal = repository.get(meal.getId());
        if (foundMeal != null && foundMeal.getUserId() != userId) {
            return null;
        }

        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal foundMeal = repository.get(id);
        return foundMeal != null && foundMeal.getUserId() == userId && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal foundMeal = repository.get(id);
        if (foundMeal == null) {
            return null;
        }
        return foundMeal.getUserId() == userId ? foundMeal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

