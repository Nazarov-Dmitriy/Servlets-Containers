package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.atomic.AtomicLong;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    @Override
    public void init() {
        final var repository = new PostRepository();
        final var service = new PostService(repository);
        controller = new PostController(service);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            if (method.equals(GET) && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches("/api/posts/\\d+")) {
                final var id = new AtomicLong(Long.parseLong(path.substring(path.lastIndexOf("/") + 1)));
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals("/api/posts")) {
                System.out.println(resp);
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches("/api/posts/\\d+")) {
                final var id = new AtomicLong(Long.parseLong(path.substring(path.lastIndexOf("/") + 1)));
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

