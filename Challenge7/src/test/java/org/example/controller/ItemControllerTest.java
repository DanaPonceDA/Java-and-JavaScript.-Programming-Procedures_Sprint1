package org.example.controller;

import com.google.gson.Gson;
import org.example.Item;
import org.example.dao.ItemDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;

import java.lang.reflect.Method; 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemControllerTest {

    private ItemDAO itemDAO;
    private Gson gson;
    private ItemController controller;
    private Request req;
    private Response res;


    @BeforeEach
    void setup() {
        itemDAO = mock(ItemDAO.class);
        gson = new Gson();
        controller = new ItemController(itemDAO, gson);
        req = mock(Request.class);  
        res = mock(Response.class);
    }

    @Test
    void testAddItemSuccess() {
        when(req.queryParams("nombre")).thenReturn("TV");
        when(req.queryParams("descripcion")).thenReturn("Smart TV");
        when(req.queryParams("price")).thenReturn("500");
        when(itemDAO.addItem(any(Item.class))).thenReturn(true);

        String result = controller.handleAddItem(req, res);

        assertNull(result);
        verify(itemDAO, times(1)).addItem(any(Item.class));
    }

    @Test
    void testAddItemFailure() {
        when(req.queryParams("nombre")).thenReturn("Laptop");
        when(req.queryParams("descripcion")).thenReturn("Gaming");
        when(req.queryParams("price")).thenReturn("1200");
        when(itemDAO.addItem(any(Item.class))).thenReturn(false);

        String result = controller.handleAddItem(req, res);
        assertTrue(result.contains("Failed to create item"));
    }

    @Test
    void testAddItemMalformedJson() {
        when(req.queryParams("nombre")).thenReturn("Mouse");
        when(req.queryParams("descripcion")).thenReturn("USB");
        when(req.queryParams("price")).thenReturn("abc"); // valor inválido
        String result = controller.handleAddItem(req, res);
        assertTrue(result.contains("Invalid price format"));
    }
}
