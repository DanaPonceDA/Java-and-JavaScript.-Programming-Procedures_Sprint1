package org.example.controller;

import com.google.gson.Gson;
import org.example.Item;
import org.example.dao.ItemDAO;
import org.example.dao.OfferDAO;
import org.example.Offer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class OfferControllerTest {

    private ItemDAO itemDAO;
    private OfferDAO offerDAO;
    private Gson gson;
    private OfferController controller;
    private Request req;
    private Response res;

    @BeforeEach
    void setup() {
        itemDAO = mock(ItemDAO.class);
        offerDAO = mock(OfferDAO.class);
        gson = new Gson();
        controller = new OfferController(itemDAO, offerDAO, gson);
        req = mock(Request.class);
        res = mock(Response.class);
    }

    @Test
    void testPostOfferSuccess() {
        when(req.queryParams("itemId")).thenReturn("item1");
        when(req.queryParams("offerPrice")).thenReturn("100.0");
        when(req.queryParams("offerUser")).thenReturn("test@example.com");

        Item mockItem = new Item("item1", "TV", "desc", 200.0);
        when(itemDAO.getItemById("item1")).thenReturn(mockItem);
        when(offerDAO.addOffer(any(Offer.class))).thenReturn(true);
        when(itemDAO.updateItem(eq("item1"), any(Item.class))).thenReturn(true);

        String result = controller.handlePostOffer(req, res);

        assertTrue(result.contains("Offer created successfully"));
        verify(offerDAO, times(1)).addOffer(any(Offer.class));
        verify(res, times(1)).redirect("/items", 303);
    }

    @Test
    void testPostOfferInvalidItem() {
        when(req.queryParams("itemId")).thenReturn("bad");
        when(req.queryParams("offerPrice")).thenReturn("100.0");
        when(req.queryParams("offerUser")).thenReturn("test@example.com");
        when(itemDAO.getItemById("bad")).thenReturn(null);

        String result = controller.handlePostOffer(req, res);

        assertTrue(result.contains("Item not found"));
        verify(res, never()).redirect(anyString(), anyInt());
    }

    @Test
    void testPostOfferInvalidPrice() {
        when(req.queryParams("itemId")).thenReturn("item1");
        when(req.queryParams("offerPrice")).thenReturn("abc");
        when(req.queryParams("offerUser")).thenReturn("test@example.com");

        String result = controller.handlePostOffer(req, res);

        assertTrue(result.contains("Invalid price"));
    }

    @Test
    void testPostOfferMissingParams() {
        when(req.queryParams("itemId")).thenReturn(null);
        when(req.queryParams("offerPrice")).thenReturn("100.0");
        when(req.queryParams("offerUser")).thenReturn(null);

        String result = controller.handlePostOffer(req, res);

        assertTrue(result.contains("Missing required parameters"));
    }
}
