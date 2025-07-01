package org.example.lacasadelartesano.modelo;

public class Artesano {
    private int id;
    private String nombre;
    private String negocio;
    private String telefono;
    private byte[] imagen;

    // Constructor con imagen
    public Artesano(int id, String nombre, String negocio, String telefono, byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.negocio = negocio;
        this.telefono = telefono;
        this.imagen = imagen;
    }

    // Constructor sin imagen (imagen null por defecto)
    public Artesano(String nombre, String negocio, String telefono) {
        this(-1, nombre, negocio, telefono, null);
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getNegocio() { return negocio; }
    public void setNegocio(String negocio) { this.negocio = negocio; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }
}
