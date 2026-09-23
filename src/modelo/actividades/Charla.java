package modelo.actividades;

public class Charla extends Actividad {
    private String disertante;

    public Charla (int id, String titulo, int cupoMaximo, String disertante) {
        super(id,titulo,cupoMaximo);
        setDisertante(disertante);
    }

    public void setDisertante(String disertante) {
        if (disertante != null && !disertante.isEmpty()) {
            this.disertante = disertante;
        }
    }

    public String getDisertante() {
        return disertante;
    }

    @Override // Redefinir el comportamiento del metodo heredado
    public double calcularCostoMateriales() {
        return 0.0; // Las charlas son gratuitas
    }

    @Override
    public String getTipo() {
        return this.getClass().getSimpleName(); // Recupera el nombre exacto de la clase dinámica en ejecución
    }
}

