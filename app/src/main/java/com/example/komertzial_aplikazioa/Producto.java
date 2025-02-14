package com.example.komertzial_aplikazioa;

public class Producto {
    private int id;          // Campo para almacenar el ID del producto
    private String izen;     // Nombre del producto
    private double prezio;   // Precio del producto
    private int stock;       // Stock disponible
    private int cantidad;    // Nueva propiedad para almacenar la cantidad seleccionada

    // Constructor
    public Producto(int id, String izena, double prezioa, int stock) {
        this.id = id;
        this.izen = izena;
        this.prezio = prezioa;
        this.stock = stock;
        this.cantidad = 0;  // Inicializamos la cantidad a 0 por defecto
    }

    public Producto() {
        this.cantidad = 0;  // Inicializamos la cantidad a 0 por defecto
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izen;
    }

    public void setNombre(String izena) {
        this.izen = izena;
    }

    public double getPrezio() {
        return prezio;
    }

    public void setPrecio(double prezioa) {
        this.prezio = prezioa;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Getter y Setter para la cantidad
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
