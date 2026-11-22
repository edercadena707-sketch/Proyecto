package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * NIVEL 2 - Autómata de Pila (determinista, acepta por estado final).
 *
 * En cada paso la máquina:
 *   1. busca una transición que lea el símbolo actual con el tope de la pila;
 *   2. si no hay, busca una transición ε (que no lee entrada);
 *   3. si tampoco hay, se detiene.
 * La pila se guarda en un String: el primer carácter es el fondo y el último es el tope.
 */
public class AutomataPila extends Maquina {
    public static final char EPSILON = '\u03b5';   // el símbolo ε
    public static final char FONDO = EPSILON;      // símbolo del fondo de la pila (se muestra como ε)
    private static final int LIMITE_PASOS = 1000;  // evita ciclos infinitos con ε

    private String estadoInicial;
    private ArrayList<String> estadosAceptacion = new ArrayList<>();
    private ArrayList<TransicionPila> transiciones = new ArrayList<>();

    public AutomataPila(String nombre, String descripcion, String estadoInicial) {
        super(nombre, descripcion);
        this.estadoInicial = estadoInicial;
    }

    public void agregarTransicion(String estado, char simbolo, char tope, String nuevoEstado, String apilar) {
        transiciones.add(new TransicionPila(estado, simbolo, tope, nuevoEstado, apilar));
    }

    public void marcarComoAceptacion(String estado) {
        estadosAceptacion.add(estado);
    }

    private TransicionPila buscarTransicion(String estado, char simbolo, char tope) {
        for (TransicionPila t : transiciones) {
            if (t.getEstado().equals(estado) && t.getSimbolo() == simbolo && t.getTope() == tope) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Resultado simular(String cadena) {
        Resultado resultado = new Resultado();
        String estado = estadoInicial;
        String pila = "" + FONDO;
        int posicion = 0;

        Paso inicial = new Paso(0, estado, "Inicio: estado " + estado + ", pila con " + FONDO);
        inicial.setPila(pila);
        resultado.agregarPaso(inicial);

        boolean seDetuvo = false;
        int contador = 0;
        while (!seDetuvo && contador < LIMITE_PASOS) {
            contador++;

            char tope = ' ';
            if (pila.length() > 0) {
                tope = pila.charAt(pila.length() - 1);
            }

            // 1 y 2: buscar transición (primero leyendo entrada, luego con ε)
            TransicionPila t = null;
            if (posicion < cadena.length()) {
                t = buscarTransicion(estado, cadena.charAt(posicion), tope);
            }
            if (t == null) {
                t = buscarTransicion(estado, EPSILON, tope);
            }

            if (t == null) {
                seDetuvo = true;   // 3: no hay nada que hacer
            } else {
                String lee;
                if (t.getSimbolo() == EPSILON) {
                    lee = "Sin leer entrada (ε)";
                } else {
                    lee = "Lee '" + t.getSimbolo() + "'";
                    posicion++;
                }
                String accion;
                if (t.getApilar().equals("")) {
                    accion = "desapila " + tope;
                } else if (t.getApilar().equals("" + tope)) {
                    accion = "la pila no cambia";
                } else {
                    accion = "cambia " + tope + " por " + t.getApilar();
                }

                // se saca el tope
                if (pila.length() > 0) {
                    pila = pila.substring(0, pila.length() - 1);
                }
                // se escribe lo nuevo (al revés, para que el primer carácter quede arriba)
                String texto = t.getApilar();
                for (int i = texto.length() - 1; i >= 0; i--) {
                    pila = pila + texto.charAt(i);
                }
                estado = t.getNuevoEstado();

                Paso paso = new Paso(posicion, estado, lee + ": " + accion + " → " + estado);
                paso.setPila(pila);
                resultado.agregarPaso(paso);
            }
        }

        if (!seDetuvo) {
            resultado.terminar(false, "Se superó el límite de " + LIMITE_PASOS + " pasos");
        } else if (posicion < cadena.length()) {
            resultado.terminar(false, "La máquina se detuvo antes de leer '" + cadena.charAt(posicion)
                    + "': no hay transición desde " + estado + " con esa entrada y ese tope");
        } else if (estadosAceptacion.contains(estado)) {
            resultado.terminar(true, "Se leyó toda la cadena y " + estado + " es estado de aceptación");
        } else {
            resultado.terminar(false, "Se leyó toda la cadena pero " + estado + " NO es estado de aceptación");
        }
        return resultado;
    }

    public String getEstadoInicial() { return estadoInicial; }
    public ArrayList<TransicionPila> getTransiciones() { return transiciones; }
}
