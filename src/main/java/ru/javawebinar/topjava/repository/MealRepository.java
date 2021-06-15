package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal, int currentUserId);

    // false if meal do not belong to userId
    boolean delete(int id, int currentUserId);

    // null if meal do not belong to userId
    Meal get(int id, int currentUserId);

    // ORDERED dateTime desc
    Collection<Meal> getAll(int currentUserId);

    Collection<Meal> getBetweenDateBoundaries(LocalDate startDate, LocalDate endDate, int currentUserId);
}
