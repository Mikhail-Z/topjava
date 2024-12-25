package ru.javawebinar.topjava.to;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.beans.ConstructorProperties;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.DATE_TIME_PATTERN;

public class MealTo extends BaseTo {

    @NotNull
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    private final LocalDateTime dateTime;

    @NotBlank
    @Size(min = 3, max = 200)
    private final String description;

    @Positive
    private final Integer calories;

    private final Boolean excess;

    @ConstructorProperties({"id", "dateTime", "description", "calories", "excess"})
    public MealTo(Integer id, LocalDateTime dateTime, String description, Integer calories, Boolean excess) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCalories() {
        return calories;
    }

    public boolean isExcess() {
        return excess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo mealTo = (MealTo) o;
        return Objects.equals(calories, mealTo.calories) &&
                excess == mealTo.excess &&
                Objects.equals(id, mealTo.id) &&
                Objects.equals(dateTime, mealTo.dateTime) &&
                Objects.equals(description, mealTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
