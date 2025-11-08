package org.example.controller;

import com.google.gson.Gson;
import org.example.dao.ItemDAO;
import org.example.Item;
import spark.Request;
import spark.Response;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class ItemController {
    private final ItemDAO itemDAO;
    private final Gson gson;

    public ItemController(ItemDAO itemDAO, Gson gson) {
        this.itemDAO = itemDAO;
        this.gson = gson;
        setupRoutes();
    }

    private void setupRoutes() {
        get("/items", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            String minPriceStr = req.queryParams("min_price");
            String maxPriceStr = req.queryParams("max_price");

            Double minPrice = minPriceStr != null && !minPriceStr.isEmpty() ? Double.parseDouble(minPriceStr) : null;
            Double maxPrice = maxPriceStr != null && !maxPriceStr.isEmpty() ? Double.parseDouble(maxPriceStr) : null;

            model.put("items", itemDAO.getItemsByPriceRange(minPrice, maxPrice));

            return new ModelAndView(model, "item_list.mustache");
        }, new MustacheTemplateEngine());

        get("/items.json", (req, res) -> {
            res.type("application/json");

            String minPriceStr = req.queryParams("min_price");
            String maxPriceStr = req.queryParams("max_price");

            Double minPrice = minPriceStr != null && !minPriceStr.isEmpty() ? Double.parseDouble(minPriceStr) : null;
            Double maxPrice = maxPriceStr != null && !maxPriceStr.isEmpty() ? Double.parseDouble(maxPriceStr) : null;

            return gson.toJson(itemDAO.getItemsByPriceRange(minPrice, maxPrice));
        });

        get("/items/new", (req, res) -> {
            return new ModelAndView(null, "item_form.mustache");
        }, new MustacheTemplateEngine());

        get("/items/:id", (req, res) -> {
            String id = req.params("id");
            Item item = itemDAO.getItemById(id);

            Map<String, Object> model = new HashMap<>();

            if (item == null) {
                res.status(404);
                model.put("errorMessage", "Item not found");
                return new ModelAndView(model, "error.mustache");
            }

            model.put("item", item);
            return new ModelAndView(model, "item_detail.mustache");
        }, new MustacheTemplateEngine());

        post("/items/:id", this::addItem);

        post("/items", (req, res) -> {
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            String priceStr = req.queryParams("price");

            double price = Double.parseDouble(priceStr);

            // Crear un nuevo Item
            Item newItem = new Item();
            newItem.setNombre(nombre);
            newItem.setDescripcion(descripcion);
            newItem.setPrice(price);

            itemDAO.addItem(newItem);

            res.redirect("/items");
            return null;
        });

        put("/items/:id", this::updateItem);

        options("/items/:id", this::checkItemExistence);

        delete("/items/:id", this::deleteItem);
    }

    private Object getAllItems(Request req, Response res) {
        res.type("application/json");
        return gson.toJson(itemDAO.getAllItems());
    }

    private Object getItemById(Request req, Response res) {
        res.type("application/json");
        String id = req.params("id");
        Item item = itemDAO.getItemById(id);
        if (item != null) {
            return gson.toJson(item);
        }
        res.status(404);
        return gson.toJson("Item not found");
    }

    private Object addItem(Request req, Response res) throws Exception {
        res.type("application/json");
        String id = req.params("id");
        Item item = gson.fromJson(req.body(), Item.class);
        item.setId(id);
        boolean created = itemDAO.addItem(item);
        res.status(created ? 201 : 400);
        return gson.toJson(created ? "Item created" : "Failed to create item");
    }

    private Object updateItem(Request req, Response res) throws Exception {
        res.type("application/json");
        String id = req.params("id");
        Item item = gson.fromJson(req.body(), Item.class);
        boolean updated = itemDAO.updateItem(id, item);
        res.status(updated ? 200 : 404);
        return gson.toJson(updated ? "Item updated" : "Item not found");
    }

    private Object checkItemExistence(Request req, Response res) throws Exception {
        res.type("application/json");
        String id = req.params("id");
        boolean exists = itemDAO.itemExists(id);
        return gson.toJson(exists);
    }

    private Object deleteItem(Request req, Response res) throws Exception {
        res.type("application/json");
        String id = req.params("id");
        boolean deleted = itemDAO.deleteItem(id);
        res.status(deleted ? 200 : 404);
        return gson.toJson(deleted ? "Item deleted" : "Item not found");
    }
}
