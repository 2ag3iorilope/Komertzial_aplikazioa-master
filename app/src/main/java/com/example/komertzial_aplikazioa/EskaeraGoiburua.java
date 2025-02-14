package com.example.komertzial_aplikazioa;

public class EskaeraGoiburua {
    private int codigoPedido;
    private String direccionEnvio;
    private String fechaPedido;
    private int idComercial;
    private int idPartner;
    private String estadoPedido;

    public EskaeraGoiburua(int codigoPedido, String direccionEnvio, String fechaPedido, int idComercial, int idPartner, String estadoPedido) {
        this.codigoPedido = codigoPedido;
        this.direccionEnvio = direccionEnvio;
        this.fechaPedido = fechaPedido;
        this.idComercial = idComercial;
        this.idPartner = idPartner;
        this.estadoPedido = estadoPedido;
    }

    // Getters y Setters
    public int getCodigoPedido() { return codigoPedido; }
    public void setCodigoPedido(int codigoPedido) { this.codigoPedido = codigoPedido; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(String fechaPedido) { this.fechaPedido = fechaPedido; }

    public int getIdComercial() { return idComercial; }
    public void setIdComercial(int idComercial) { this.idComercial = idComercial; }

    public int getIdPartner() { return idPartner; }
    public void setIdPartner(int idPartner) { this.idPartner = idPartner; }

    public String getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(String estadoPedido) { this.estadoPedido = estadoPedido; }

    @Override
    public String toString() {
        return "EskaeraGoiburua{" +
                "codigoPedido=" + codigoPedido +
                ", direccionEnvio='" + direccionEnvio + '\'' +
                ", fechaPedido='" + fechaPedido + '\'' +
                ", idComercial=" + idComercial +
                ", idPartner=" + idPartner +
                ", estadoPedido='" + estadoPedido + '\'' +
                '}';
    }
}

