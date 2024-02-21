package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Meal MEAL_1 = new Meal(START_SEQ+3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_2 = new Meal(START_SEQ+4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_3 = new Meal(START_SEQ+5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
    public static final Meal MEAL_4 = new Meal(START_SEQ+6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL_5 = new Meal(START_SEQ+7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_6 = new Meal(START_SEQ+8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_7 = new Meal(START_SEQ+9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410);

    public static final Meal MEAL_8 = new Meal(START_SEQ+10, LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_9 = new Meal(START_SEQ+11, LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед", 1000);
    public static final Meal MEAL_10 = new Meal(START_SEQ+12, LocalDateTime.of(2020, Month.FEBRUARY, 1, 20, 0), "Ужин", 500);
    public static final Meal MEAL_11 = new Meal(START_SEQ+13, LocalDateTime.of(2020, Month.FEBRUARY, 2, 0, 0), "Еда на граничное значение", 100);
    public static final Meal MEAL_12 = new Meal(START_SEQ+14, LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_13 = new Meal(START_SEQ+15, LocalDateTime.of(2020, Month.FEBRUARY, 2, 13, 0), "Обед", 500);
    public static final Meal MEAL_14 = new Meal(START_SEQ+16, LocalDateTime.of(2020, Month.FEBRUARY, 2, 20, 0), "Ужин", 410);


    public static final List<Meal> USER_MEALS = Arrays.asList(
            MEAL_1,
            MEAL_2,
            MEAL_3,
            MEAL_4,
            MEAL_5,
            MEAL_6,
            MEAL_7
    );

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            MEAL_8,
            MEAL_9,
            MEAL_10,
            MEAL_11,
            MEAL_12,
            MEAL_13,
            MEAL_14
    );

    public static final int NOT_FOUND = START_SEQ + 10000;

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2023, 1, 1, 10, 10), "new meal", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}
