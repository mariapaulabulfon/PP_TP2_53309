package modelo.actividades;
import modelo.Estudiante;
import modelo.certificacion.Certificable;

public class Curso extends Actividad implements Certificable {
    private int nivel;

    public Curso(int id, String titulo, int cupoMaximo, int nivel) {
        super(id, titulo, cupoMaximo);
        this.nivel = nivel;
    }

    @Override
    public double calcularCostoMateriales() {
        // Metodo polimorfico
        switch (nivel) {
            case 1:
                return 1000.0;
            case 2:
                return 2000.0;
            case 3:
                return 3000.0;
            default:
                return 0.0;
        }
    }

    @Override
    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String generarCertificado(Estudiante estudiante) {
        return "CERTIFICADO DE ASISTENCIA EMITIDO POR " + ENTIDAD_EMISORA +
                "Otorgado a: " + estudiante.getNombre() + " (Legajo: " + estudiante.getLegajo() + ")\n" +
                "Por su participación en el Curso: " + this.getTitulo() + " del nivel " + nivel;
    }
}
