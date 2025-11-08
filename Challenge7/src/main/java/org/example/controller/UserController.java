package org.example.controller;

import com.google.gson.Gson;
import org.example.dao.UserDAO;
import org.example.User;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class UserController {
    private final UserDAO userDAO;
    private final Gson gson;

    public UserController(UserDAO userDAO, Gson gson) {
        this.userDAO = userDAO;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {

        get("/users", this::getAllUsers);

        get("/users/:id", this::getUserById);

        post("/users/:id", this::addUser);

        put("/users/:id", this::updateUser);

        options("/users/:id", this::checkUserExistence);

        delete("/users/:id", this::deleteUser);
    }


    private Object getAllUsers(Request req, Response res) throws Exception {
        res.type("application/json");
        return gson.toJson(userDAO.getAllUsers());
    }

    private Object getUserById(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            int id = Integer.parseInt(req.params("id"));
            User user = userDAO.getUserById(id);
            if (user != null) {
                return gson.toJson(user);
            } else {
                res.status(404);
                return gson.toJson("User not found");
            }
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson("Invalid ID format");
        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Internal server error");
        }
    }

    private Object addUser(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            int id = Integer.parseInt(req.params("id"));
            User user = gson.fromJson(req.body(), User.class);
            user.setId(id);
            boolean created = userDAO.addUser(user);
            res.status(created ? 201 : 400);
            return gson.toJson(created ? "User created" : "Failed to create user");
        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Internal server error");
        }
    }

    private Object updateUser(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            int id = Integer.parseInt(req.params("id"));
            User user = gson.fromJson(req.body(), User.class);
            boolean updated = userDAO.updateUser(id, user);
            res.status(updated ? 200 : 404);
            return gson.toJson(updated ? "User updated" : "User not found");
        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Internal server error");
        }
    }

    private Object checkUserExistence(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            int id = Integer.parseInt(req.params("id"));
            boolean exists = userDAO.userExists(id);
            return gson.toJson(exists);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(false);
        }
    }

    private Object deleteUser(Request req, Response res) throws Exception {
        res.type("application/json");
        try {
            int id = Integer.parseInt(req.params("id"));
            boolean deleted = userDAO.deleteUser(id);
            res.status(deleted ? 200 : 404);
            return gson.toJson(deleted ? "User deleted" : "User not found");
        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Internal server error");
        }
    }
}