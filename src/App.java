import excepciones.CupoExcedidoException;
import modelo.*;
import modelo.certificacion.Certificable;
import modelo.actividades.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

        public static void main(String[] args) {

                // 1. Crear Lista de Estudiantes
                List<Estudiante> estudiantes = new ArrayList<>();
                Estudiante est1 = new Estudiante("34567", "Ana Gómez");
                Estudiante est2 = new Estudiante("51786", "Carlos Pérez");
                Estudiante est3 = new Estudiante("47658", "María Rodríguez");
                Estudiante est4 = new Estudiante("88990", "Juan López");

                estudiantes.add(est1);
                estudiantes.add(est2);
                estudiantes.add(est3);
                estudiantes.add(est4);

                // 2. Crear Eventos
                EventoUniversitario evento1 = new EventoUniversitario(
                        "E001",
                        "Jornada de Tecnología Java",
                        5000.0,
                        false
                );

                EventoUniversitario evento2 = new EventoUniversitario(
                        "E002",
                        "Taller de Programación Libre",
                        0.0,
                        true
                );

                EventoUniversitario evento3 = new EventoUniversitario(
                        "E003",
                        "Hackaton 2026",
                        7000.0,
                        false
                );

                // 3. Crear Salas y Asignarlas (Agregación)
                Sala sala1 = new Sala(1, "Aula Magna");
                Sala sala2 = new Sala(2, "Sala de Conferencias");
                Sala sala3 = new Sala(3, "Laboratorio de Informática");

                evento1.asignarSala(sala1);
                evento2.asignarSala(sala2);
                evento3.asignarSala(sala3);

                // 4. Crear Actividades Polimórficas (Composición)
                evento1.crearActividad("charla", 1, "Introducción a Java 21", 30, "Ing. Juan Pérez", false);
                evento1.crearActividad("taller", 2, "Taller de POO Avanzada", 2, "", true); // Cupo reducido a 2 para testear el desborde
                evento1.crearActividad("curso", 3, "Curso de Desarrollo Backend", 10, "", false); // Nuevo tipo Curso (Ejercicio 2)

                evento2.crearActividad("charla", 3, "Software Libre en las Universidades", 40, "Lic. Marta Ramos", false);

                // 5. Inscribir Estudiantes y Manejo de CupoExcedidoException (try-catch-finally)
                System.out.println("\n=== PROCESANDO INSCRIPCIONES ===");
                try {
                        if (!evento1.getActividades().isEmpty()) {
                                Actividad charla1 = evento1.getActividades().get(0);
                                Actividad taller1 = evento1.getActividades().get(1);
                                Actividad curso1 = evento1.getActividades().get(2);

                                System.out.println("Inscribiendo estudiantes en Charla...");
                                charla1.inscribir(est1);
                                charla1.inscribir(est2);

                                System.out.println("Inscribiendo estudiantes en Taller...");
                                taller1.inscribir(est1);
                                taller1.inscribir(est2);

                                System.out.println("Inscribiendo estudiantes en Curso...");
                                curso1.inscribir(est3);
                                curso1.inscribir(est4);

                                System.out.println("Intentando inscribir estudiante extra en Taller (Supera el cupo)...");
                                taller1.inscribir(est3); // Dispara CupoExcedidoException
                        }
                } catch (CupoExcedidoException e) {
                        System.out.println("\nError al inscribir: " + e.getMessage());
                } finally {
                        System.out.println("Finalizado el proceso de inscripción. \n");
                }

                if (!evento2.getActividades().isEmpty()) {
                        try {
                                Actividad charla2 = evento2.getActividades().get(0);
                                charla2.inscribir(est1);
                                charla2.inscribir(est3);
                        } catch (CupoExcedidoException e) {
                                System.out.println("Error al inscribir: " + e.getMessage());
                        }
                }

                // 6. Persistir el evento mediante Serialización (Granularidad de Excepciones)
                System.out.println("\n*** Almacenando el evento ID: " + evento1.getId() + " ***");
                try {
                        evento1.persistirEvento();
                        System.out.println("Evento guardado correctamente en disco.");
                } catch (FileNotFoundException e) {
                        System.out.println("Imposible guardar el evento ID: " + evento1.getId() + ". Error al guardar el archivo: " + e.getMessage());
                } catch (IOException e) {
                        System.out.println("Imposible guardar el evento ID: " + evento1.getId() + ".");
                        e.printStackTrace();
                }

                // 7. Recuperar el evento desde el archivo .dat
                System.out.println("\n*** Recuperando el evento ID: " + evento1.getId() + " almacenado previamente ***");
                try {
                        EventoUniversitario eventoRecuperado = EventoUniversitario.recuperarEvento(evento1.getId());
                        if (eventoRecuperado != null) {
                                System.out.println("Evento reconstruido exitosamente desde el archivo.");
                                System.out.println("\n=== DATOS DEL EVENTO RECUPERADO ===");
                                eventoRecuperado.mostrarDatos();
                        }
                } catch (ClassNotFoundException e) {
                        System.out.println("No fue posible reconstruir el objeto almacenado: " + e.getMessage());
                } catch (FileNotFoundException e) {
                        System.out.println("Imposible recuperar el evento ID: " + evento1.getId() + ". Archivo no encontrado: " + e.getMessage());
                } catch (IOException e) {
                        System.out.println("Imposible recuperar el evento ID: " + evento1.getId() + ".");
                        e.printStackTrace();
                }

                // EJERCICIO 2: EMISIÓN Y MUESTRA DE CERTIFICADOS (INTERFACES)
                System.out.println("\n=================================================");
                System.out.println("       EMISIÓN DE CERTIFICADOS DE ASISTENCIA     ");
                System.out.println("=================================================");

                // Recorre las actividades del evento y emite certificados únicamente si implementan Certificable (Talleres y Cursos)
                for (Actividad act : evento1.getActividades()) {
                        if (act instanceof Certificable certificable) {
                                for (Inscripcion ins : act.getInscripciones()) {
                                        String certificado = certificable.generarCertificado(ins.getEstudiante());
                                        System.out.println(certificado);
                                        System.out.println("-------------------------------------------------");
                                }
                        }
                }

                // Filtrado por tipo (Generics acotados), cantidad por tipo y costo de materiales
                System.out.println("\n=================================================");
                System.out.println("  FILTRADO Y COSTOS DE MATERIALES  ");
                System.out.println("=================================================");

                // Filtrado que devuelve listas correctamente tipadas
                List<Charla> listaCharlas = evento1.filtrarActividadesPorTipo(Charla.class);
                List<Taller> listaTalleres = evento1.filtrarActividadesPorTipo(Taller.class);
                List<Curso> listaCursos = evento1.filtrarActividadesPorTipo(Curso.class);

                // Mostrar por consola la cantidad de actividades de cada tipo
                System.out.println("Evento ID: " + evento1.getId() + " (" + evento1.getTitulo() + "):");
                System.out.println(" - Cantidad de Charlas: " + listaCharlas.size());
                System.out.println(" - Cantidad de Talleres: " + listaTalleres.size());
                System.out.println(" - Cantidad de Cursos: " + listaCursos.size());

                // Calcular y mostrar el costo de materiales correspondiente a cada tipo usando el metodo con Wildcard
                System.out.println("\nCosto de materiales por tipo de actividad en Evento ID: " + evento1.getId() + " (" + evento1.getTitulo() + "):");
                System.out.println(" - Costo materiales Charlas: $" + evento1.calcularCostoMateriales(listaCharlas));
                System.out.println(" - Costo materiales Talleres: $" + evento1.calcularCostoMateriales(listaTalleres));
                System.out.println(" - Costo materiales Cursos: $" + evento1.calcularCostoMateriales(listaCursos));

                // Costo de materiales acumulado de todas las actividades
                System.out.println(" - Costo TOTAL de materiales del evento: $" + evento1.calcularCostoMateriales(evento1.getActividades()));

                // 8. Constructor de Copia y Muestra de Datos de Memoria
                EventoUniversitario copiaEvento1 = new EventoUniversitario(evento1);

                System.out.println("\n=== EVENTO 1 ===");
                evento1.mostrarDatos();

                System.out.println("\n=== EVENTO 2 ===");
                evento2.mostrarDatos();

                System.out.println("\n=== EVENTO 3 ===");
                evento3.mostrarDatos();

                System.out.println("\n=== COPIA DEL EVENTO 1 ===");
                copiaEvento1.mostrarDatos();

                System.out.println("\nCantidad total de eventos creados: " + EventoUniversitario.getCantEventos());
        }
}
