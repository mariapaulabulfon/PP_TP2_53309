package modelo;

import java.io.Serializable;

public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L; // Recomendado para versiones de serialización
    private String legajo;
    private String nombre;

    // Constructor
    public Estudiante(String legajo, String nombre) {
        setLegajo(legajo);
        setNombre(nombre);
    }

    public void setLegajo(String legajo) {
        if (legajo != null && !legajo.isEmpty()) {
            this.legajo = legajo;
        }
    }

    public String getLegajo() {
        return legajo;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.isEmpty()) {
            this.nombre = nombre;
        }
    }

    public String getNombre() {
        return nombre;
    }
}
