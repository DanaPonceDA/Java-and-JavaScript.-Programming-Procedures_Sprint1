package org.example.dao;

import org.example.Offer;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import org.example.DatabaseTestHelper;

import static org.junit.jupiter.api.Assertions.*;

class OfferDAOTest {

    private Connection connection; // CAMBIO: Ahora es una conexión de instancia, no estática
    private OfferDAO offerDAO;

    @BeforeEach // CAMBIO CLAVE: Usamos BeforeEach para aislar cada prueba
    void setup() throws Exception {
        // 1. Abrir una nueva conexión H2 en memoria para esta prueba
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        // 2. Crear la tabla en la base de datos de esta prueba
        Statement stmt = connection.createStatement();
        // Nota: Añadimos DROP TABLE IF EXISTS para la limpieza, aunque H2:mem debería ser suficiente
        stmt.execute("DROP TABLE IF EXISTS offers");
        stmt.execute("CREATE TABLE offers (id INT AUTO_INCREMENT PRIMARY KEY, itemId VARCHAR(255), offerPrice DOUBLE, offerUser VARCHAR(255))");
        stmt.close();

        // 3. Establecer la conexión en el Helper para que el DAO la use
        DatabaseTestHelper.setConnection(connection);
        offerDAO = new OfferDAO();
    }

    @AfterEach // CAMBIO CLAVE: Usamos AfterEach para limpiar la conexión de esta prueba
    void tearDown() throws Exception {
        // Cerrar la conexión, eliminando la base de datos en memoria para el siguiente test
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testAddAndGetOffers() throws SQLException {
        Offer offer = new Offer(0, "item1", 99.99, "user@example.com");
        assertTrue(offerDAO.addOffer(offer));
        assertTrue(offer.getId() > 0);

        List<Offer> offers = offerDAO.getAllOffers();
        assertFalse(offers.isEmpty());
    }
}