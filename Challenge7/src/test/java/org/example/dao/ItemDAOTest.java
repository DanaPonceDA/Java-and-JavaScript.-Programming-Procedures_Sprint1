package org.example.dao;

import org.example.Database;
import org.example.model.Item;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

    private ItemDAO itemDAO;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        conn.createStatement().execute("""
            CREATE TABLE items (
                id VARCHAR(50) PRIMARY KEY,
                nombre VARCHAR(100),
                descripcion VARCHAR(255),
                price DOUBLE
            )
        """);
        Database.setConnectionForTest(conn); // optional helper method in Database.java
    }

    @BeforeEach
    void setUp() {
        itemDAO = new ItemDAO();
    }

    @Test
    void testAddAndRetrieveItem() {
        Item item = new Item("1", "Laptop", "Gaming laptop", 1200.0);
        assertTrue(itemDAO.addItem(item));

        Item retrieved = itemDAO.getItemById("1");
        assertNotNull(retrieved);
        assertEquals("Laptop", retrieved.getNombre());
    }

    @Test
    void testUpdateItem() {
        Item item = new Item("2", "Mouse", "Wireless", 25.0);
        itemDAO.addItem(item);

        item.setPrice(30.0);
        assertTrue(itemDAO.updateItem("2", item));

        Item updated = itemDAO.getItemById("2");
        assertEquals(30.0, updated.getPrice());
    }

    @Test
    void testDeleteItem() {
        Item item = new Item("3", "Keyboard", "Mechanical", 80.0);
        itemDAO.addItem(item);
        assertTrue(itemDAO.deleteItem("3"));
        assertNull(itemDAO.getItemById("3"));
    }
}
