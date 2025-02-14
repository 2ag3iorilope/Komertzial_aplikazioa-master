package com.example.komertzial_aplikazioa;

public class ProductoDetalle {
    private int codigoPedido;  // Asegúrate de tener este campo
    private int codigoProducto;
    private String nombreProducto;
    private double precioUnitario;
    private int cantidad;

    // Constructor
    public ProductoDetalle(int codigoPedido, int codigoProducto, String nombreProducto, double precioUnitario, int cantidad) {
        this.codigoPedido = codigoPedido;  // Inicializamos el código de pedido
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getCodigoPedido() {
        return codigoPedido;  // Este método es necesario para obtener el código de pedido
    }

    public void setCodigoPedido(int codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
