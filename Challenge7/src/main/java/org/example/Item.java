package org.example;

public class Item {

    String id;
    String nombre;
    String descripcion;
    double price;

    public Item() {
    }

    public Item(String id, String nombre, String descripcion, double price) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.price = price;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }






}