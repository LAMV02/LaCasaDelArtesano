package org.example.lacasadelartesano.modelo;

public class Producto {
    private int id;
    private int idArtesano;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio;

    public Producto(int id, int idArtesano, String nombre, String descripcion, int stock, double precio) {
        this.id = id;
        this.idArtesano = idArtesano;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
    }

    public Producto(int idArtesano, String nombre, String descripcion, int stock, double precio) {
        this(-1, idArtesano, nombre, descripcion, stock, precio);
    }



    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getArtesanoId() { return idArtesano; }
    public void setIdArtesano(int idArtesano) { this.idArtesano = idArtesano; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getInventario() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}
