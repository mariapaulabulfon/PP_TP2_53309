package modelo.actividades;

import modelo.certificacion.Certificable;
import modelo.Estudiante;

public class Taller extends Actividad implements Certificable {
    private boolean requiereNotebook;

    public Taller(int id, String titulo, int cupoMaximo, boolean requiereNotebook) {
        super(id, titulo,cupoMaximo);
        this.requiereNotebook = requiereNotebook;
    }

    public boolean isRequiereNotebook() {
        return requiereNotebook;
    }

    public void setRequiereNotebook(boolean requiereNotebook) {
        this.requiereNotebook = requiereNotebook;
    }

    @Override
    public double calcularCostoMateriales() {
        // $5000 si requiere uso de notebook y $2000 si no requiere
        return requiereNotebook ? 5000.0 : 2000.0;
    }

    @Override
    public String getTipo() {
        return this.getClass().getSimpleName(); // Recupera el nombre exacto de la clase dinámica en ejecución;
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA EMITIDO POR " + ENTIDAD_EMISORA +
                "Otorgado a: " + estudiante.getNombre() + " (Legajo: " + estudiante.getLegajo() + ")\n" +
                "Por su participación en el Taller: " + getTitulo();
    }
}

