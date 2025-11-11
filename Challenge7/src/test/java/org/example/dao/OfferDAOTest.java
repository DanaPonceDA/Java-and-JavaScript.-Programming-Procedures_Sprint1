package org.example.dao;

import org.example.Offer;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;
import org.example.DatabaseTestHelper;

import static org.junit.jupiter.api.Assertions.*;

class OfferDAOTest {

    private Connection connection; 
    private OfferDAO offerDAO;

    @BeforeEach 
    void setup() throws Exception {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        Statement stmt = connection.createStatement();
        stmt.execute("DROP TABLE IF EXISTS offers");
        stmt.execute("CREATE TABLE offers (id INT AUTO_INCREMENT PRIMARY KEY, itemId VARCHAR(255), offerPrice DOUBLE, offerUser VARCHAR(255))");
        stmt.close();

        DatabaseTestHelper.setConnection(connection);
        offerDAO = new OfferDAO();
    }

    @AfterEach 
    void tearDown() throws Exception {
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
