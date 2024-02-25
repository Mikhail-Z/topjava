package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final Meal meal1 = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак USER", 500);
    public static final Meal meal2 = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед USER", 1000);
    public static final Meal meal3 = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин USER", 500);
    public static final Meal meal4 = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение USER", 100);
    public static final Meal meal5 = new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак USER", 1000);
    public static final Meal meal6 = new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед USER", 500);
    public static final Meal meal7 = new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин USER", 410);

    public static final Meal meal8 = new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.FEBRUARY, 1, 10, 0), "Завтрак ADMIN", 500);
    public static final Meal meal9 = new Meal(START_SEQ + 11, LocalDateTime.of(2020, Month.FEBRUARY, 1, 13, 0), "Обед ADMIN", 1000);
    public static final Meal meal10 = new Meal(START_SEQ + 12, LocalDateTime.of(2020, Month.FEBRUARY, 1, 20, 0), "Ужин ADMIN", 500);
    public static final Meal meal11 = new Meal(START_SEQ + 13, LocalDateTime.of(2020, Month.FEBRUARY, 2, 0, 0), "Еда на граничное значение ADMIN", 100);
    public static final Meal meal12 = new Meal(START_SEQ + 14, LocalDateTime.of(2020, Month.FEBRUARY, 2, 10, 0), "Завтрак ADMIN", 1000);
    public static final Meal meal13 = new Meal(START_SEQ + 15, LocalDateTime.of(2020, Month.FEBRUARY, 2, 13, 0), "Обед ADMIN", 500);
    public static final Meal meal14 = new Meal(START_SEQ + 16, LocalDateTime.of(2020, Month.FEBRUARY, 2, 20, 0), "Ужин ADMIN", 410);


    public static final List<Meal> USER_MEALS = Arrays.asList(
            meal1, meal2, meal3, meal4, meal5, meal6, meal7
    );

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(
            meal8, meal9, meal10, meal11, meal12, meal13, meal14
    );

    public static final int NOT_FOUND = START_SEQ + 10000;
    public static final int NEW_MEAL_ID = START_SEQ + 17;

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
