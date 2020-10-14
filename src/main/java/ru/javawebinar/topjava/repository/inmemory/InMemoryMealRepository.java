package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> this.save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> map = repository.getOrDefault(userId, new HashMap<>());

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            map.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return map.computeIfPresent(meal.getId(), (id, oldMeal) -> oldMeal = meal);
    }

    @Override
    public boolean delete(int userId, int mealId) {
        if (!repository.containsKey(userId)) {
            return false;
        }
        return repository.get(userId).remove(mealId) != null;
    }

    @Override
    public Meal get(int userId, int mealId) {
        if (!repository.containsKey(userId)) {
            return null;
        }
        return repository.get(userId).get(mealId);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        if (!repository.containsKey(userId)) {
            return Collections.emptyList();
        }
        return repository
                .get(userId)
                .values()
                .stream()
                .sorted((meal1, meal2) -> meal2.getDate().compareTo(meal1.getDate()))
                .collect(Collectors.toList());
    }
}

