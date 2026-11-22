package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * Clase padre de las tres máquinas (AFD, autómata de pila y Máquina de Turing).
 * Guarda lo que tienen en común: nombre, descripción y cadenas de ejemplo.
 * Cada hija debe implementar su propio método simular().
 */
public abstract class Maquina {
    private String nombre;
    private String descripcion;
    private ArrayList<String> aceptadas = new ArrayList<>();
    private ArrayList<String> rechazadas = new ArrayList<>();

    public Maquina(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /** Procesa la cadena completa y devuelve todos los pasos. */
    public abstract Resultado simular(String cadena);

    public void agregarAceptada(String cadena) { aceptadas.add(cadena); }
    public void agregarRechazada(String cadena) { rechazadas.add(cadena); }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public ArrayList<String> getAceptadas() { return aceptadas; }
    public ArrayList<String> getRechazadas() { return rechazadas; }

    // Se usa para que el JComboBox muestre el nombre de la máquina
    @Override
    public String toString() { return nombre; }
}
