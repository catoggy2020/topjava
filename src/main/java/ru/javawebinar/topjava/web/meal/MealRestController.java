package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        return getAllFiltered(LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX);
    }

    public Collection<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getTos(service.getAllFiltered(authUserId(), startDate, endDate, startTime, endTime), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        return service.create(authUserId(), meal);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal);
    }
}