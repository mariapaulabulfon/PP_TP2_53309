package modelo;

import java.io.Serializable;

public class Sala implements Serializable {
    private int id;
    private String nombre;

    // Constructor
    public Sala (int id, String nombre) {
        setId(id);
        setNombre(nombre);
    }

    public void setId(int id) {
        if (id > 0) {
            this.id = id;
        } else {
            System.out.println("El ID de la sala debe ser un entero positivo.");
        }
    }

    public int getId() {
        return id;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            this.nombre = nombre;
        } else {
            System.out.println("No puede ingresarse como nombre una cadena vacia.");
        }
    }

    public String getNombre() {
        return this.nombre;
    }
}
