package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MealServlet extends HttpServlet {

    private Dao dao;

    @Override
    public void init() throws ServletException {
        dao = new Dao();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action") == null? "" : request.getParameter("action");
        String id = request.getParameter("id") == null? "" : request.getParameter("id");

        switch (action) {
            case "delete" : {
                dao.delete(Integer.parseInt(id));
                break;
            }

            case "update" : {
                Meal meal = dao.getMeal(Integer.parseInt(id));
                request.setAttribute("id", id);
                request.setAttribute("date", meal.getDate());
                request.setAttribute("time", meal.getTime());
                request.setAttribute("description", meal.getDescription());
                request.setAttribute("calories", meal.getCalories());
                request.getRequestDispatcher("/addAndUpdate.jsp").forward(request, response);
            }
            default : {
            }
        }

        List<MealTo> mealTos = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id").equals("")? "-1" : request.getParameter("id");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String description = request.getParameter("description");
        String calories = request.getParameter("calories");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(date + " " + time, formatter);

        dao.createOrUpdate(new Meal(Integer.parseInt(id), dateTime, description, Integer.parseInt(calories)));

        List<MealTo> mealTos = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("meals", mealTos);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
