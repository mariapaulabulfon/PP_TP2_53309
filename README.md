# Trabajo Práctico Nº 2 - Paradigmas de Programación

**Estudiante:** Maria Paula Bulfon  
**Legajo:** 53309  
**Materia:** Paradigmas de Programación  
**Carrera:** Ingeniería en Sistemas de Información  
**Año:** 2º año  
**Universidad:** Universidad Tecnológica Nacional - Facultad Regional Mendoza  
**Lenguaje:** Java 21  

---

## Descripción del proyecto

Este proyecto corresponde al **Trabajo Práctico N.º 2** de la materia Paradigmas de Programación.

El trabajo consiste en la evolución y escalabilidad del sistema de administración de **eventos universitarios** desarrollado previamente, incorporando características avanzadas de la **Programación Orientada a Objetos (POO)** en Java.

El sistema modulariza su estructura para mejorar los niveles de encapsulamiento mediante paquetes, fortalece el diseño ante fallos utilizando excepciones personalizadas y chequeadas, implementa la persistencia de datos mediante serialización y deserialización de objetos en disco, incorpora interfaces para el comportamiento de certificación y utiliza métodos genéricos acotados y comodines (*wildcards*).

> **Aviso de Alcance de Entrega:** Según lo estipulado por el profesor, esta entrega comprende **únicamente hasta el Ejercicio 3 inclusive**. El Ejercicio 4 (correspondiente a hilos y clases anidadas) no se incluye en esta entrega y se completará en la fecha posterior fijada por los docentes. 
---

## Objetivos

A través de los ejercicios que integran esta entrega se aplican los siguientes conceptos avanzados:

* **Modularización y Encapsulamiento:** Organización del código en paquetes (`modelo`, `modelo.actividades`, `excepciones`, `certificacion`) estructurados según responsabilidades.
* **Manejo de Excepciones Chequeadas:** Creación de excepciones personalizadas (`CupoExcedidoException`), uso explícito de `throw` y declaración `throws`, y control granular mediante bloques `try-catch-finally`.
* **Persistencia de Objetos:** Preservación del estado de los objetos en disco y su posterior reconstrucción mediante serialización (`ObjectOutputStream`) y deserialización (`ObjectInputStream`) con manejo granular de errores de E/S (`IOException`, `FileNotFoundException`, `ClassNotFoundException`).
* **Interfaces y Contratos:** Definición e implementación de la interfaz `Certificable` para desacoplar comportamiento dinámico en clases específicas (`Taller` y `Curso`).
* **Parametrización y Genéricos:** Uso de métodos genéricos acotados (`<T extends Actividad>`) y tipos comodín (*wildcards* `List<? extends Actividad>`) para procesamiento flexible y de tipo seguro.

---

# Ejercicios implementados

## Ejercicio 1 - Organización en Paquetes, Excepciones y Persistencia

En el primer ejercicio se reestructura la arquitectura del proyecto y se agrega tolerancia a fallos y persistencia.

### Estructura de paquetes
El proyecto se organiza en los siguientes paquetes:
* `modelo`: Clases del dominio principal (`EventoUniversitario`, `Sala`, `Estudiante`, `Inscripcion`).
* `modelo.actividades`: Jerarquía de actividades (`Actividad`, `Charla`, `Taller`, `Curso`).
* `excepciones`: Excepciones personalizadas del sistema.
* `certificacion`: Interfaces para emisión de certificados.

### Manejo de excepciones personalizadas
Se crea la excepción chequeada `CupoExcedidoException` que hereda de `Exception`. 
* En la clase `Actividad`, el método `inscribir(Estudiante estudiante)` evalúa el cupo disponible contra `cupoMaximo`. Si el cupo está agotado, interrumpe el flujo lanzando `CupoExcedidoException` con `throw` y declarándolo en su firma mediante `throws`.
* En la clase principal `App`, el proceso de inscripción se envuelve en una estructura `try-catch-finally` que captura la excepción, muestra mensajes claros en consola y permite que el programa continúe su ejecución normal.

### Persistencia mediante serialización
Se implementa la interfaz `Serializable` en las clases del modelo.
* `EventoUniversitario` incorpora el método `persistirEvento()` para guardar el estado del objeto en un archivo binario `.dat`.
* A su vez, ofrece el método estático `recuperarEvento(String id)` para deserializar y reconstruir la instancia guardada.
* Se implementa un control granular de excepciones para la lectura/escritura en disco (`FileNotFoundException`, `IOException` y `ClassNotFoundException`).

---

## Ejercicio 2 - Interfaz `Certificable` y Actividad `Curso`

En el segundo ejercicio se incorpora la capacidad de emitir certificados de asistencia únicamente para aquellas actividades que sean de tipo certificable.

### Interfaz `Certificable`
Ubicada en el paquete `certificacion`, define la constante `ENTIDAD_EMISORA` y la firma del método:
```java
String generarCertificado(Estudiante estudiante); 
```

### Nueva actividad Curso
Se crea la clase Curso dentro del paquete modelo.actividades, la cual hereda de Actividad e implementa la interfaz Certificable.

### Jerarquía y Comportamiento de Certificación
* Taller y Curso: Implementan Certificable y generan certificados de asistencia personalizados para los estudiantes inscriptos.

* Charla: Hereda de Actividad, pero no implementa Certificable (las charlas no emiten certificados).

* En el método main de App, se evalúan las actividades del evento mediante el operador instanceof para filtrar de forma polimórfica únicamente las actividades certificables y emitir sus diplomas correspondientes.

## Ejercicio 3 - Métodos Genéricos Acotados y Comodines (Wildcards)
En el tercer ejercicio se incorporan métodos genéricos en la clase EventoUniversitario para operar sobre las colecciones de actividades respetando la seguridad de tipos.

### Filtrado por Tipo Concreto (Generics Acotados)
Se implementa el método parametrizado:

```java
public <T Actividad extends> List<T> filtrarActividadesPorTipo(Class<T> tipo)
```
Este método permite filtrar y retornar listas del tipo exacto solicitado (List<Charla>, List<Taller> o List<Curso>), garantizando colecciones fuertemente tipadas sin necesidad de casteos explícitos externos.

### Cálculo de Costo de Materiales con Comodines (Wildcards)
Se implementa el método:

```java
public double calcularCostoMateriales(List<? extends Actividad> actividades)
```
Haciendo uso de comodines acotados superiormente (? extends Actividad), el método puede recibir tanto la lista general de actividades del evento como sublistas de cualquier tipo derivado específico (List<Taller>, List<Curso>) y calcular la suma de sus costos de materiales de forma polimórfica.

## Ejercicio 4 - Planificación de Próxima Entrega
El Ejercicio 4, correspondiente a la implementación de clases anidadas (TicketDeAcceso) y la ejecución concurrente con hilos secundario (EnvioTicketsThread), no está incluido en la presente entrega por disposición de la cátedra y se agregará en el siguiente plazo de entrega.

## Estructura del proyecto
```text
PP_TP2_53309/
│
├── src/
│   ├── App.java                         # Clase principal con el método main (Punto de entrada)
│   │
│   ├── certificacion/
│   │   └── Certificable.java            # Interfaz para actividades que emiten certificado
│   │
│   ├── excepciones/
│   │   └── CupoExcedidoException.java   # Excepción personalizada de cupo
│   │
│   ├── modelo/
│   │   ├── Estudiante.java              # Entidad estudiante
│   │   ├── EventoUniversitario.java     # Entidad gestora del evento, persistencia y genéricos
│   │   ├── Inscripcion.java             # Clase asociativa
│   │   └── Sala.java                    # Entidad de espacio físico
│   │
│   └── modelo/actividades/
│       ├── Actividad.java               # Clase abstracta base de actividades
│       ├── Charla.java                  # Subclase concreta (sin costo de materiales)
│       ├── Curso.java                   # Subclase concreta (implementa Certificable)
│       └── Taller.java                  # Subclase concreta (implementa Certificable)
│
├── evento_E001.dat                      # Archivo binario generado por la serialización
├── Captura de pantalla (1)              # Captura de la salida por consola de una ejecución del programa
├── Captura de pantalla (2)              # Captura de la salida por consola de una ejecución del programa
├── Captura de pantalla (3)              # Captura de la salida por consola de una ejecución del programa
└── Captura de pantalla (4)              # Captura de la salida por consola de una ejecución del programa
```

## Descripción de los componentes principales

| Componente | Tipo | Descripción |
| :--- | :--- | :--- |
| `EventoUniversitario` | Clase | Gestiona el evento, agregación con `Sala`, composición de `Actividad`, serialización `.dat` y operaciones con Genéricos. |
| `Actividad` | Clase Abstracta | Base para actividades. Lanza `CupoExcedidoException` al validar cupo en `inscribir()`. |
| `Charla` | Clase | Especialización de `Actividad` para exposiciones. |
| `Taller` | Clase | Especialización de `Actividad`. Implementa `Certificable`. |
| `Curso` | Clase | Nueva especialización de `Actividad`. Implementa `Certificable`. |
| `CupoExcedidoException` | Excepción | Excepción chequeada lanzada al intentar sobrepasar el `cupoMaximo` de una actividad. |
| `Certificable` | Interfaz | Define el contrato para la generación e impresión de certificados de asistencia. |
| `App` | Clase Executable | Contiene el flujo del método `main`, gestionando las pruebas de los ejercicios 1, 2 y 3. |

## Requisitos
Para ejecutar el proyecto se requiere:

* JDK 21

* IntelliJ IDEA u otro IDE compatible con Java.

* Git, en caso de querer clonar el repositorio.

## Ejecución
Clonar el repositorio
```bash
git clone [https://github.com/mariapaulabulfon/PP_TP2_53309.git](https://github.com/mariapaulabulfon/PP_TP2_53309.git)
```

Luego ingresar al directorio:

```bash
cd PP_TP2_53309
```
### Ejecutar el programa
1. Abrir el proyecto en IntelliJ IDEA.

2. Verificar que la estructura de SDK apunte a Java 21.

3. Abrir el archivo src/App.java.

4. Ejecutar el método main.

5. Comprobar en la consola la secuencia de logs:

* Control y captura de excepciones de cupo (CupoExcedidoException).

* Persistencia y lectura de archivos .dat vía serialización.

* Impresión de certificados emitidos para Talleres y Cursos.

* Filtrado dinámico por tipo mediante Generics y cómputo de materiales con Wildcards.

## Tecnologías utilizadas
* Java 21

* Programación Orientada a Objetos (POO)

* Manejo de Excepciones y Persistencia de Objetos (Serialización)

* Generics y Wildcards (Parametrización acotada)

* IntelliJ IDEA

* Git & GitHub
