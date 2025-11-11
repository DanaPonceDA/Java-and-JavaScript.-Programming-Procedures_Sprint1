package org.example.controller;

import org.example.dao.ItemDAO;
import org.example.dao.OfferDAO;
import org.example.Offer;
import org.example.Item;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;

import static spark.Spark.*;

public class OfferController {

    private final ItemDAO itemDAO;
    private final OfferDAO offerDAO;
    private final Gson gson;

    public OfferController(ItemDAO itemDAO, OfferDAO offerDAO, Gson gson) {
        this.itemDAO = itemDAO;
        this.offerDAO = offerDAO;
        this.gson = gson;

        setupRoutes();
    }

    private void setupRoutes() {
        get("/offers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            List<Offer> offers = offerDAO.getAllOffers();

            List<Map<String, Object>> offersWithNames = new ArrayList<>();
            for (Offer offer : offers) {
                Map<String, Object> o = new HashMap<>();
                o.put("id", offer.getId());
                o.put("itemId", offer.getItemId());
                o.put("offerPrice", offer.getOfferPrice());
                o.put("offerUser", offer.getOfferUser());

                Item item = itemDAO.getItemById(offer.getItemId());
                o.put("itemName", item != null ? item.getNombre() : "Desconocido");

                offersWithNames.add(o);
            }

            model.put("offers", offersWithNames);
            return new ModelAndView(model, "offer_list.mustache");
        }, new MustacheTemplateEngine());

        get("/offers/new", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("items", itemDAO.getAllItems());
            return new ModelAndView(model, "offer_form.mustache");
        }, new MustacheTemplateEngine());

        post("/offers", this::handlePostOffer);
    }

    public String handlePostOffer(Request req, Response res) {
        try {
            String itemId = req.queryParams("itemId");
            String offerPriceStr = req.queryParams("offerPrice");
            String offerUser = req.queryParams("offerUser");

            // Validaciones básicas
            if (itemId == null || offerPriceStr == null || offerUser == null) {
                res.status(400);
                return gson.toJson("Missing required parameters");
            }

            double offerPrice;
            try {
                offerPrice = Double.parseDouble(offerPriceStr);
            } catch (NumberFormatException e) {
                res.status(400);
                return gson.toJson("Invalid price format");
            }

            Item item = itemDAO.getItemById(itemId);
            if (item == null) {
                res.status(404);
                return gson.toJson("Item not found");
            }

            Offer offer = new Offer(itemId, offerUser, offerPrice);
            boolean offerCreated = offerDAO.addOffer(offer);

            if (!offerCreated) {
                res.status(500);
                return gson.toJson("Failed to create offer");
            }

            // ✅ Actualizar el precio del item si es necesario
            item.setPrice(offerPrice);
            itemDAO.updateItem(itemId, item);

            res.status(201);
            // ✅ Redirigir al listado de items
            res.redirect("/items", 303);
            return gson.toJson("Offer created successfully");

        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            return gson.toJson("Error processing offer");
        }
    }
}
