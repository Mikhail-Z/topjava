package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init() {
        try (ClassPathXmlApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            mealRestController = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        MealTo mealTo = new MealTo(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                false);

        log.info(mealTo.getId() == null ? "Create {}" : "Update {}", mealTo);

        if (mealTo.getId() == null) {
            mealRestController.create(mealTo);
        } else {
            mealRestController.update(mealTo);
        }

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = parseInt(request, "id");
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo mealTo = "create".equals(action) ?
                        new MealTo(null, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, false) :
                        mealRestController.get(parseInt(request, "id"));
                request.setAttribute("meal", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                LocalDate startDate = parseDate(request, "startDate");
                LocalTime startTime = parseTime(request, "startTime");
                LocalDate endDate = parseDate(request, "endDate");
                LocalTime endTime = parseTime(request, "endTime");
                log.info("filter");
                request.setAttribute("meals", mealRestController.getBetweenTimeBoundaries(startDate, startTime, endDate, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", new ArrayList<>(mealRestController.getAll()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int parseInt(HttpServletRequest request, String paramName) {
        String number = Objects.requireNonNull(request.getParameter(paramName));
        return Integer.parseInt(number);
    }

    private LocalDate parseDate(HttpServletRequest request, String paramName) {
        String rawDate = Objects.requireNonNull(request.getParameter(paramName));
        return LocalDate.parse(rawDate);
    }

    public LocalTime parseTime(HttpServletRequest request, String paramName) {
        String rawTime = Objects.requireNonNull(request.getParameter(paramName));
        return LocalTime.parse(rawTime);
    }
}
