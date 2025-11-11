package org.example.dao;

import org.example.Database;
import org.example.DatabaseTestHelper;
import org.example.Offer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfferDAO {

    // 🔹 MÉTODO DE UTILIDAD: Usa la conexión de pruebas si existe, o la real en producción
    private Connection getCurrentConnection() throws SQLException {
        Connection testConn = DatabaseTestHelper.getConnection();
        if (testConn != null) {
            return testConn; // conexión inyectada por los tests
        }
        return Database.getConnection(); // conexión de producción
    }

    // ✅ CAMBIO CLAVE 1: ya no lanza SQLException
    // En lugar de 'throws SQLException', ahora manejamos internamente los errores
    public boolean addOffer(Offer offer) {
        String query = "INSERT INTO offers (itemId, offerPrice, offerUser) VALUES (?, ?, ?)";

        try {
            // 🔹 Obtenemos la conexión fuera del try-with-resources (la maneja Spark o el test)
            Connection conn = getCurrentConnection();

            // 🔹 try-with-resources solo para PreparedStatement
            try (PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                pstmt.setString(1, offer.getItemId());
                pstmt.setDouble(2, offer.getOfferPrice());
                pstmt.setString(3, offer.getOfferUser());

                int rowsAffected = pstmt.executeUpdate();

                // 🔹 Si se insertó correctamente, obtener el ID generado
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
            // ✅ CAMBIO CLAVE 2: Capturamos y registramos SQLException internamente
            e.printStackTrace();
            return false; // devolvemos false si hay error en base de datos
        }
    }

    // ✅ Método sin cambios funcionales importantes, solo manejo de errores consistente
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
            // ✅ CAMBIO CLAVE 3: manejo interno de SQLException
            e.printStackTrace();
            return Collections.emptyList();
        }

        return offers;
    }

    // 🔹 (Opcional) podrías agregar más métodos como getOfferById, deleteOffer, etc.
}
