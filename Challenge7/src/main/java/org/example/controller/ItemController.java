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

        get("/items/new", (req, res) -> new ModelAndView(null, "item_form.mustache"), new MustacheTemplateEngine());

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

        // ✅ Cambiamos de this::addItem a this::handleAddItem (nuevo método público para testing)
        post("/items", this::handleAddItem);

        put("/items/:id", this::updateItem);
        options("/items/:id", this::checkItemExistence);
        delete("/items/:id", this::deleteItem);
    }

    // ✅ Nuevo método público para las pruebas unitarias
    // Replica la lógica de addItem() pero con un nombre accesible
    public String handleAddItem(Request req, Response res) {
        try {
            res.type("application/json");

            // 🔹 Leer los valores del formulario (NO JSON)
            String nombre = req.queryParams("nombre");
            String descripcion = req.queryParams("descripcion");
            String priceStr = req.queryParams("price");

            if (nombre == null || descripcion == null || priceStr == null) {
                res.status(400);
                return gson.toJson("Missing required fields");
            }

            double price = Double.parseDouble(priceStr);

            // 🔹 Crear el objeto Item
            Item item = new Item();
            item.setNombre(nombre);
            item.setDescripcion(descripcion);
            item.setPrice(price);

            // 🔹 Insertar en la base de datos
            boolean created = itemDAO.addItem(item);

            res.status(created ? 201 : 400);
            if (created) {
                // ✅ Si el item se creó correctamente, redirige al listado de ítems
                res.redirect("/items");
                return null; // Spark ignora el return si ya se hizo redirect
            } else {
                res.status(400);
                return gson.toJson("Failed to create item");
            }
        } catch (NumberFormatException e) {
            res.status(400);
            return gson.toJson("Invalid price format");

        } catch (Exception e) {
            res.status(500);
            return gson.toJson("Unexpected error: " + e.getMessage());
        }
    }

    // 🔒 Dejamos addItem() privado para uso interno, no se usa en tests
    private Object addItem(Request req, Response res) throws Exception {
        res.type("application/json");
        String id = req.params("id");
        Item item = gson.fromJson(req.body(), Item.class);
        item.setId(id);
        boolean created = itemDAO.addItem(item);
        res.status(created ? 201 : 400);
        if (created) {
            // ✅ Si el item se creó correctamente, redirige al listado de ítems
            res.redirect("/items");
            return null; // Spark ignora el return si ya se hizo redirect
        } else {
            res.status(400);
            return gson.toJson("Failed to create item");
        }    }

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
