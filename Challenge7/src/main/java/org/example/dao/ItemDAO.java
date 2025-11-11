package org.example.dao;

import org.example.Database;
import org.example.Item;
import org.example.DatabaseTestHelper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private Connection getCurrentConnection() throws SQLException {
        Connection testConn = DatabaseTestHelper.getConnection();
        if (testConn != null) {
            return testConn;
        }
        return Database.getConnection();
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, price FROM items";

        try {
            Connection conn = getCurrentConnection();

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    items.add(new Item(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("price")
                    ));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public Item getItemById(String id) {
        String sql = "SELECT id, nombre, descripcion, price FROM items WHERE id = ?";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);

                try(ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new Item(
                                rs.getString("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("price")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Item> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT id, nombre, descripcion, price FROM items WHERE 1=1");

        if (minPrice != null) sql.append(" AND price >= ?");
        if (maxPrice != null) sql.append(" AND price <= ?");

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

                int index = 1;
                if (minPrice != null) pstmt.setDouble(index++, minPrice);
                if (maxPrice != null) pstmt.setDouble(index++, maxPrice);

                try(ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        items.add(new Item(
                                rs.getString("id"),
                                rs.getString("nombre"),
                                rs.getString("descripcion"),
                                rs.getDouble("price")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean addItem(Item item) {
        String sql = "INSERT INTO items (id, nombre, descripcion, price) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, item.getId());
                pstmt.setString(2, item.getNombre());
                pstmt.setString(3, item.getDescripcion());
                pstmt.setDouble(4, item.getPrice());

                int rows = pstmt.executeUpdate();
                return rows > 0;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateItem(String id, Item item) {
        String sql = "UPDATE items SET nombre = ?, descripcion = ?, price = ? WHERE id = ?";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, item.getNombre());
                pstmt.setString(2, item.getDescripcion());
                pstmt.setDouble(3, item.getPrice());
                pstmt.setString(4, id);

                int rows = pstmt.executeUpdate();
                return rows > 0;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteItem(String id) {
        String sql = "DELETE FROM items WHERE id = ?";

        try {
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);
                int rows = pstmt.executeUpdate();
                return rows > 0;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean itemExists(String id) {
        String sql = "SELECT COUNT(*) FROM items WHERE id = ?";

        try {
            // CAMBIO CLAVE: Obtener la conexión fuera del try-with-resources
            Connection conn = getCurrentConnection();

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);

                // Usar try-with-resources para el ResultSet
                try(ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
