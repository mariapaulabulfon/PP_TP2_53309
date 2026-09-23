package modelo.actividades;

import excepciones.CupoExcedidoException;
import modelo.Estudiante;
import modelo.Inscripcion;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Actividad implements Serializable {
    private int id;
    private String titulo;
    private int cupoMaximo;
    public static final int CUPO_MINIMO; // Constante fija según UML (se usa internamente, no va en el constructor)
    private List<Inscripcion> inscripciones;

    static {
       CUPO_MINIMO = 0;
    }

    // Constructor
    public Actividad (int id, String titulo, int cupoMaximo) {
        this.id = id;
        setTitulo(titulo);
        setCupoMaximo(cupoMaximo);
        this.inscripciones = new ArrayList<>();
    }

    public void setTitulo(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            this.titulo = titulo;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        if (cupoMaximo >= CUPO_MINIMO) {
            this.cupoMaximo = cupoMaximo;
        } else {
            this.cupoMaximo = CUPO_MINIMO; // Asigna el mínimo por defecto si se envía un valor menor
        }
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    // Metodo de inscripción usando 'this' para asociar esta actividad
    public Inscripcion inscribir(Estudiante estudiante) throws CupoExcedidoException {
        if (inscripciones.size() >= cupoMaximo) {
            // Se lanza la excepción interrumpiendo el flujo normal
            throw new CupoExcedidoException("No se puede inscribir al estudiante " + estudiante.getNombre() + ". Cupo máximo alcanzado.");
        }
        Inscripcion inscripcion = new Inscripcion(LocalDate.now(),"Inscripto", estudiante, this);
        this.inscripciones.add(inscripcion);
        return inscripcion;
    }

    // Impresión detallada de la lista de inscritos
    public void mostrarInscripciones () {
        System.out.println("Inscripciones para la actividad '" + titulo + "':");
        if (inscripciones.isEmpty()) {
            System.out.println("No hay estudiantes inscriptos.");
        } else {
            for (Inscripcion ins : inscripciones) {
                System.out.println("- Estudiante: " + ins.getEstudiante().getNombre() +
                        " | Legajo: " + ins.getEstudiante().getLegajo() +
                        " | Fecha: " + ins.getFecha() +
                        " | Estado: " + ins.getEstado());
            }
        }
    }

    // Metodo FINAL según el diagrama UML: no se puede sobrescribir en subclases
    public final void mostrarIdentificacion() {
        System.out.println("[" + getTipo().toUpperCase() + "] ID: " + id + " - Título: " + titulo + " (Cupo máx: " + cupoMaximo + ")");
    }

    // Métodos ABSTRACTOS que deben implementar las subclases modelo.actividades.Charla y modelo.actividades.Taller
    public abstract double calcularCostoMateriales();
    public abstract String getTipo();
}

