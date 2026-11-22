package MASAMORRA.logica;

/**
 * La cinta de la Máquina de Turing. Aquí está la ÚNICA diferencia entre el nivel 0 y el nivel 1:
 *
 *  - Cinta extensible (nivel 0): si el cabezal sale por un lado, se agrega un blanco y la cinta crece.
 *  - Cinta acotada (nivel 1): la entrada queda entre dos delimitadores, [ al inicio y ] al final.
 *    El cabezal no puede salirse de ahí ni borrar los delimitadores.
 */
public class Cinta {
    public static final char BLANCO = '_';
    public static final char INICIO = '[';
    public static final char FIN = ']';

    private StringBuilder celdas = new StringBuilder();
    private boolean acotada;
    private int cabezal;

    public Cinta(String entrada, boolean acotada) {
        this.acotada = acotada;
        if (acotada) {
            celdas.append(INICIO);
            celdas.append(entrada);
            celdas.append(FIN);
            cabezal = 1;                 // primer símbolo de la entrada
        } else {
            celdas.append(entrada);
            if (entrada.length() == 0) {
                celdas.append(BLANCO);   // la cinta nunca queda sin celdas
            }
            cabezal = 0;
        }
    }

    public char leer() {
        return celdas.charAt(cabezal);
    }

    /** Escribe en la celda actual. Devuelve false si intenta borrar un delimitador. */
    public boolean escribir(char simbolo) {
        char actual = leer();
        if (acotada && (actual == INICIO || actual == FIN) && simbolo != actual) {
            return false;
        }
        celdas.setCharAt(cabezal, simbolo);
        return true;
    }

    /** Mueve el cabezal ('L' izquierda, 'R' derecha, 'S' se queda). Devuelve false si se sale de la cinta acotada. */
    public boolean mover(char direccion) {
        if (direccion == 'R') {
            if (cabezal == celdas.length() - 1) {
                if (acotada) {
                    return false;
                }
                celdas.append(BLANCO);       // la cinta crece a la derecha
            }
            cabezal++;
        } else if (direccion == 'L') {
            if (cabezal == 0) {
                if (acotada) {
                    return false;
                }
                celdas.insert(0, BLANCO);    // la cinta crece a la izquierda (el cabezal sigue en 0)
            } else {
                cabezal--;
            }
        }
        return true;
    }

    public String getContenido() { return celdas.toString(); }
    public int getCabezal() { return cabezal; }
}
