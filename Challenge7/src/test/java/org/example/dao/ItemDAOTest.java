package org.example.dao;

import org.example.Item;
import org.junit.jupiter.api.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import org.example.DatabaseTestHelper;

import static org.junit.jupiter.api.Assertions.*;

class ItemDAOTest {

    private Connection connection; // CAMBIO: Ahora es una conexión de instancia
    private ItemDAO itemDAO;

    @BeforeEach // CAMBIO CLAVE: Usamos BeforeEach para una DB limpia por test
    void setupDatabase() throws Exception {
        // 1. Abrir una nueva conexión H2 para esta prueba
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        // 2. Crear la tabla
        Statement stmt = connection.createStatement();
        stmt.execute("DROP TABLE IF EXISTS items");
        stmt.execute("CREATE TABLE items (id VARCHAR(255) PRIMARY KEY, nombre VARCHAR(255), descripcion TEXT, price DOUBLE)");
        stmt.close();

        // 3. Establecer la conexión en el Helper
        DatabaseTestHelper.setConnection(connection);
        itemDAO = new ItemDAO();
    }

    @AfterEach // CAMBIO CLAVE: Usamos AfterEach para cerrar la conexión de esta prueba
    void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddAndGetItem() {
        Item item = new Item("1", "Laptop", "Gaming", 1500.0);
        assertTrue(itemDAO.addItem(item));

        Item fetched = itemDAO.getItemById("1");
        assertNotNull(fetched);
        assertEquals("Laptop", fetched.getNombre());
    }

    @Test
    void testUpdateItem() {
        Item item = new Item("2", "Tablet", "Android", 300.0);
        itemDAO.addItem(item);

        item.setPrice(350.0);
        assertTrue(itemDAO.updateItem("2", item));

        Item updated = itemDAO.getItemById("2");
        assertEquals(350.0, updated.getPrice());
    }

    @Test
    void testDeleteItem() {
        Item item = new Item("3", "Mouse", "Wireless", 25.0);
        itemDAO.addItem(item);
        assertTrue(itemDAO.deleteItem("3"));
        assertNull(itemDAO.getItemById("3"));
    }

    @Test
    void testItemExists() {
        Item item = new Item("4", "Keyboard", "Mechanical", 80.0);
        itemDAO.addItem(item);
        assertTrue(itemDAO.itemExists("4"));
        assertFalse(itemDAO.itemExists("999"));
    }
}