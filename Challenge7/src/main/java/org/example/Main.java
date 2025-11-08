package org.example;

import static spark.Spark.*;
import com.google.gson.Gson;
import org.example.controller.ItemController;
import org.example.controller.UserController;
import org.example.controller.OfferController;
import org.example.dao.ItemDAO;
import org.example.dao.UserDAO;
import org.example.dao.OfferDAO;
import java.sql.SQLException;
import org.example.controller.PriceWebSocket;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        exception(NumberFormatException.class, (exception, req, res) -> {
            res.status(400);
            res.type("application/json");
            res.body(gson.toJson("Error de   formato: El ID debe ser un número entero."));
        });

        exception(Exception.class, (exception, req, res) -> {
            res.status(500);
            res.type("application/json");
            res.body(gson.toJson("Error interno del servidor: " + exception.getMessage()));
        });

        UserDAO userDAO = new UserDAO();
        ItemDAO itemDAO = new ItemDAO();
        OfferDAO offerDAO = new OfferDAO();

        staticFiles.location("/public");

        PriceWebSocket.setItemDAO(itemDAO);
        webSocket("/ws", PriceWebSocket.class);
        init();

        new UserController(userDAO, gson);
        new ItemController(itemDAO, gson);
        new OfferController(itemDAO, offerDAO, gson);
    }
}