package com.example.komertzial_aplikazioa;

public class EskaeraXehetasuna {
    private int codigoPedido;
    private int codigoProducto;
    private double precioUnitario;
    private double total;
    private int cantidad;

    public EskaeraXehetasuna(int codigoPedido, int codigoProducto, double precioUnitario, double total, int cantidad) {
        this.codigoPedido = codigoPedido;
        this.codigoProducto = codigoProducto;
        this.precioUnitario = precioUnitario;
        this.total = total;
        this.cantidad = cantidad;
    }

    // Getters y Setters
    public int getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(int codigoPedido) { this.codigoPedido = codigoPedido; }

    public int getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(int codigoProducto) { this.codigoProducto = codigoProducto; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return "EskaeraXehetasuna{" +
                "codigoPedido=" + codigoPedido +
                ", codigoProducto=" + codigoProducto +
                ", precioUnitario=" + precioUnitario +
                ", total=" + total +
                ", cantidad=" + cantidad +
                '}';
    }
}
