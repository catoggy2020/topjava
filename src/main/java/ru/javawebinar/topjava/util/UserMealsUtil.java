package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Collector;

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

//        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

        List<UserMealWithExcess> mealsTo = filteredByCycle(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : meals) {
            LocalDate currentKey = meal.getDateTime().toLocalDate();
            int mealCalories = meal.getCalories();
            int calories = map.getOrDefault(currentKey, 0);
            if (calories == 0) {
                map.put(currentKey, mealCalories);
            } else {
                map.merge(currentKey, mealCalories, Integer::sum);
            }
        }

        List<UserMealWithExcess> result = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean isExcess = false;
                if (map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                    isExcess = true;
                }
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess));
            }
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> map = meals
                                        .stream()
                                        .collect(Collectors.toMap(
                                                meal -> meal.getDateTime().toLocalDate(),
                                                UserMeal::getCalories,
                                                Integer::sum
                                        ));
        return meals
                .stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal ->
                        new UserMealWithExcess(
                                meal.getDateTime(),
                                meal.getDescription(),
                                meal.getCalories(),
                                map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycle(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        meals.sort(Comparator.comparing(userMeal -> userMeal.getDateTime().toLocalDate()));

        List<UserMealWithExcess> result = new ArrayList<>();
        LocalDate currentLocalDate = null;
        LocalDate previousLocalDate = null;
        int caloriesTotalDay = 0;

        for (int i = 0; i < meals.size(); i++) {
            UserMeal meal = meals.get(i);
            previousLocalDate = currentLocalDate;
            currentLocalDate = meal.getDateTime().toLocalDate();
            if (previousLocalDate == null || previousLocalDate.compareTo(currentLocalDate) != 0) {
                caloriesTotalDay = getTotalCaloriesPerDay(meals, currentLocalDate, i);
            }
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), caloriesTotalDay > caloriesPerDay));
            }
        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStream(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals
                .stream()
                .collect(new MyCollector(startTime, endTime, caloriesPerDay));
    }

    private static int getTotalCaloriesPerDay(List<UserMeal> list, LocalDate localDate, int index) {
        if (index == list.size() - 1) {
            return list.get(index).getCalories();
        }
        if (localDate.compareTo(list.get(index).getDateTime().toLocalDate()) != 0) {
            return 0;
        }
        return list.get(index).getCalories() + getTotalCaloriesPerDay(list, localDate, index + 1);
    }

    private static class MyCollector implements Collector<UserMeal, List<UserMeal>, List<UserMealWithExcess>> {
        private Map<LocalDate, Integer> map;
        private LocalTime startTime;
        private LocalTime endTime;
        private int caloriesPerDay;

        public MyCollector(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.caloriesPerDay = caloriesPerDay;
            map = new HashMap<>();
        }

        @Override
        public Supplier<List<UserMeal>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<UserMeal>, UserMeal> accumulator() {
            return (meals, meal) -> {
                meals.add(meal);
                map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            };
        }

        @Override
        public BinaryOperator<List<UserMeal>> combiner() {
            return (left, right) -> {
                left.addAll(right);
                return left;
            };
        }

        @Override
        public Function<List<UserMeal>, List<UserMealWithExcess>> finisher() {
            return (meals) -> meals
                    .stream()
                    .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                    .map(meal -> new UserMealWithExcess(
                            meal.getDateTime(),
                            meal.getDescription(),
                            meal.getCalories(),
                            meal.getCalories() > caloriesPerDay
                    ))
                    .collect(Collectors.toList());
        }

        @Override
        public Set<Characteristics> characteristics() {
            return EnumSet.of(Characteristics.CONCURRENT);
        }
    }
}
