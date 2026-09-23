package modelo;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import modelo.actividades.*;  // Importa EventoUniversitario, Sala, Estudiante, etc.

public class EventoUniversitario implements Serializable {
    private final String id;
    private String titulo;
    private double costoBase;
    private boolean gratuito;
    private static int cantEventos;
    private Sala sala; // Relación de Agregación (1)
    private List<Actividad> actividades; // Relación de Composición (1..*)

    // Constructor
    public EventoUniversitario(String id, String titulo, double costoBase, boolean gratuito) {
        this.id = id;
        setTitulo(titulo);
        setCostoBase(costoBase);
        setGratuito(gratuito);
        this.actividades = new ArrayList<>(); // Inicializar la lista internamente
        ++cantEventos;
    }

    // Constructor de copia
    public EventoUniversitario(EventoUniversitario otroEvento) {
        this.id = otroEvento.id + "_copia"; // Se diferencia el ID para evitar duplicados
        this.titulo = otroEvento.titulo;
        this.costoBase = otroEvento.costoBase;
        this.gratuito = otroEvento.gratuito;
        this.sala = otroEvento.sala; // Comparte la misma sala (Agregación)
        // Copiar la lista de actividades para mantener la Composición
        this.actividades = new ArrayList<>();
        for (Actividad act : otroEvento.actividades) {
            this.actividades.add(act);
        }
        ++cantEventos;
    }

    public void setTitulo(String titulo) {
        if (titulo != null && !titulo.isEmpty()) {
            this.titulo = titulo;
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public void setCostoBase(double costoBase) {
        if (costoBase >= 0) {
            this.costoBase = costoBase;
        }
    }

    public double getCostoBase() {
        return costoBase;
    }

    public void setGratuito(boolean gratuito) {
        this.gratuito = gratuito;
    }

    public boolean isGratuito() {
        return gratuito;
    }

    public static int getCantEventos() {
        return cantEventos;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public Sala getSala(){
        return sala;
    }

    public void asignarSala(Sala sala) {
        this.sala = sala;
    }

    static {
        cantEventos = 0;
    }

    // Cálculo Polimórfico del Costo Estimado con IVA 21%
    public double calcularCostoEstimado() {
        if (gratuito) {
            return 0.0;
        }

        double sumaMateriales = 0.0;
        for (Actividad act : actividades) {
            sumaMateriales += act.calcularCostoMateriales(); // Invocación Polimórfica
        }

        return (costoBase + sumaMateriales) * 1.21;
    }


    // Metodo polimórfico para crear actividades
    public void crearActividad(String tipo, int id, String titulo, int cupoMaximo, String disertante, boolean requiereNotebook) {
        Actividad nuevaActividad = null; // Variable referenciada al tipo abstracto Padre

        switch (tipo.toLowerCase()) {       // Convierte todo el texto de una cadena a minusculas
            case "charla":
                nuevaActividad = new Charla(id, titulo, cupoMaximo, disertante); // Instanciación de subclase concreta
                break;
            case "taller":
                nuevaActividad = new Taller(id, titulo, cupoMaximo, requiereNotebook); // Instanciación de subclase concreta
                break;
            case "curso":
                nuevaActividad = new Curso(id, titulo, cupoMaximo, 1);
                break;
            default:
                System.out.println("Error: El tipo de actividad '" + tipo + "' no es válido.");
                return;
        }

        this.actividades.add(nuevaActividad); // Agregación a la colección compuesta
        System.out.println("Se creó la actividad (" + tipo + ") '" + titulo + "' en el evento '" + this.titulo + "'");
    }

    public void mostrarDatos() {
        System.out.println("Datos del evento:");
        System.out.println("Id: " + id);
        System.out.println("Título: " + titulo);

        if (gratuito) {
            System.out.println("El evento es gratuito.");
        } else {
            System.out.println("El costo estimado (con IVA 21%) es: $" + String.format("%.2f", calcularCostoEstimado()));
        }

        // Mostrar modelo.Sala asignada (Agregación)
        System.out.println("Sala: " + (sala != null ? sala.getNombre() : "Sin asignar"));

        // Recorrer y mostrar Actividades e Inscripciones (Composición y Asociación)
        System.out.println("Actividades:");
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades creadas");
        } else {
            for (Actividad act : actividades) {
                act.mostrarIdentificacion(); // Invocación del metodo FINAL
                System.out.println("Costo Materiales: $" + act.calcularCostoMateriales());
                act.mostrarInscripciones(); // Muestra la lista de inscriptos
            }
        }
    }

    // Persistir evento mediante serialización de objetos (en un archivo .dat en disco)
    public boolean persistirEvento() throws IOException {
        String nombreArchivo = "evento_" + this.id + ".dat";
        FileOutputStream ofos = null;
        ObjectOutputStream oos = null;
        try {
            ofos = new FileOutputStream(nombreArchivo);
            oos = new ObjectOutputStream(ofos);
            oos.writeObject(this);
        } finally {
            if (oos != null) {
                oos.close();
            } else if (ofos != null) {
                ofos.close();
            }
        }
        return true;
    }

    // Recuperar evento deserializando desde archivo
    public static EventoUniversitario recuperarEvento(String id) throws IOException, ClassNotFoundException {
        String nombreArchivo = "evento_" + id + ".dat";
        EventoUniversitario eventoRecuperado = null;
        FileInputStream ofis = null;
        ObjectInputStream ois = null;
        try {
            ofis = new FileInputStream(nombreArchivo);
            ois = new ObjectInputStream(ofis);
            eventoRecuperado = (EventoUniversitario) ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            } else if (ofis != null) {
                ofis.close();
            }
        }
        return eventoRecuperado;
    }

    // Metodo generico acotado para filtrar actividades por tipo de clase
    public <T extends Actividad> List<T> filtrarActividadesPorTipo(Class<T> tipo) {
        List<T> filtradas = new ArrayList<>();
        for (Actividad act : actividades) {
            if (tipo.isInstance(act)) {
                filtradas.add(tipo.cast(act));
            }
        }
        return filtradas;
    }

    // Metodo con Wildcard para calcular costo de materiales de cualquier lista de actividades
    public double calcularCostoMateriales(List<? extends Actividad> listaActividades) {
        double total = 0.0;
        for (Actividad act : listaActividades) {
            total += act.calcularCostoMateriales();
        }
        return total;
    }
}

