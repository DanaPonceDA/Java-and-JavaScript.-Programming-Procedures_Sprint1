package org.example;

public class Offer {
    private int id; // ID de la oferta
    private String itemId; // ID del Item al que aplica la oferta
    private double offerPrice; // Precio ofrecido
    private String offerUser;

    public Offer() {}

    public Offer(int id, String itemId, double offerPrice, String offerUser) {
        this.id = id;
        this.itemId = itemId;
        this.offerPrice = offerPrice;
        this.offerUser = offerUser;
    }
    public Offer(String itemId, String offerUser, double offerPrice) {
        this.itemId = itemId;
        this.offerUser = offerUser;
        this.offerPrice = offerPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferUser() {
        return offerUser;
    }

    public void setOfferUser(String offerUser) {
        this.offerUser = offerUser;
    }
}
