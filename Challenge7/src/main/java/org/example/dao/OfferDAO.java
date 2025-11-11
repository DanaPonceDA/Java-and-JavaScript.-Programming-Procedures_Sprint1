package org.example.dao;

import org.example.Database;
import org.example.DatabaseTestHelper;
import org.example.Offer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfferDAO {

    private Connection getCurrentConnection() throws SQLException {
        Connection testConn = DatabaseTestHelper.getConnection();
        if (testConn != null) {
            return testConn; // conexión inyectada por los tests
        }
        return Database.getConnection(); // conexión de producción
    }

    public boolean addOffer(Offer offer) {
        String query = "INSERT INTO offers (itemId, offerPrice, offerUser) VALUES (?, ?, ?)";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, offer.getItemId());
                pstmt.setDouble(2, offer.getOfferPrice());
                pstmt.setString(3, offer.getOfferUser());

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            offer.setId(rs.getInt(1));
                        }
                    }
                }
                return rowsAffected > 0;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Offer> getAllOffers() {
        List<Offer> offers = new ArrayList<>();
        String query = "SELECT id, itemId, offerPrice, offerUser FROM offers";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Offer offer = new Offer(
                            rs.getInt("id"),
                            rs.getString("itemId"),
                            rs.getDouble("offerPrice"),
                            rs.getString("offerUser")
                    );
                    offers.add(offer);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return offers;
    }
}
