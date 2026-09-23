package modelo;

import java.io.Serializable;
import java.time.LocalDate;
import modelo.actividades.Actividad;

public class Inscripcion implements Serializable {
    private LocalDate fecha;
    private String estado;
    private Estudiante estudiante;
    private Actividad actividad;

    public Inscripcion(LocalDate fecha, String estado, Estudiante estudiante, Actividad actividad) {
        setFecha(fecha);
        setEstado(estado);
        this.estudiante = estudiante;
        this.actividad = actividad;
    }

    // Getters y Setters de los atributos propios
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha =(fecha != null) ? fecha : LocalDate.now();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado != null && !estado.isEmpty()) {
            this.estado = estado;
        }
    }

    // Getters indispensables para consultar la relación desde fuera
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Actividad getActividad() {
        return actividad;
    }
}

