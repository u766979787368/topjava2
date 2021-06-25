package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getParameter("user")) {
            case "1" : SecurityUtil.setUser(1);
            break;
            case "2" : SecurityUtil.setUser(2);
            break;
        }
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }
}
