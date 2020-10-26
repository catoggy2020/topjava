package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

public class MealTestData {
    public static final int NOT_FOUND = 10;

    public static final Meal mealAdminLunch = new Meal(START_SEQ + 2, LocalDateTime.of(2020, Month.JANUARY, 6, 14, 0), "Админ ланч", 510);
    public static final Meal mealAdminSupper = new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 6, 21, 0), "Админ ужин", 1500);
    public static final Meal mealUserBreakfast = new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 6, 9, 0), "Завтрак", 300);
    public static final Meal mealUserDinner = new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 6, 13, 0), "Обед", 1000);
    public static final Meal mealUserSupper = new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 6, 18, 0), "Ужин", 200);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, 10, 26, 15, 0), "New meal", 200);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(mealAdminLunch.getId(), mealAdminLunch.getDateTime(), mealAdminLunch.getDescription(), mealAdminLunch.getCalories());
        updated.setDescription("UpdatedDescription");
        updated.setCalories(300);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
