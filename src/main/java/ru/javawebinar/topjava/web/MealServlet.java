package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.crud.CRUDImpl;
import ru.javawebinar.topjava.crud.CRUDRepository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final CRUDRepository crudRepository;

    public MealServlet() {
        super();
        crudRepository = new CRUDImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward;
        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            action = "listMeals";
        }

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("id"));
            crudRepository.deleteMeal(mealId);
            forward = LIST_MEALS;
            request.setAttribute("meals", MealsUtil.getMealsToList(crudRepository.getAllMeals()));
            log.debug(String.format("deleted meal id=%d", mealId));
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("id"));
            Meal meal = crudRepository.getMealById(mealId);
            request.setAttribute("meal", meal);
            log.debug(String.format("edited meal id=%d", mealId));
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEALS;
            request.setAttribute("meals", MealsUtil.getMealsToList(crudRepository.getAllMeals()));
            log.debug("get list meals");
        } else {
            forward = INSERT_OR_EDIT;
            log.debug("insert or edit meal");
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime").replace('T', ' '), dateTimeFormatter);
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        String mealId = request.getParameter("id");
        if (mealId == null || mealId.isEmpty()) {
            crudRepository.addMeal(new Meal(0, dateTime, description, calories));
            log.debug("added new meal");
        } else {
            int id = Integer.parseInt(mealId);
            crudRepository.updateMeal(new Meal(id, dateTime, description, calories));
            log.debug(String.format("updated meal id=%d", id));
        }

        RequestDispatcher view = request.getRequestDispatcher(LIST_MEALS);
        request.setAttribute("meals", MealsUtil.getMealsToList(crudRepository.getAllMeals()));
        view.forward(request, response);
    }
}
