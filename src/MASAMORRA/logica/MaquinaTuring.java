package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * NIVELES 1 y 0 - Máquina de Turing determinista de una cinta.
 *  - acotada = true  -> nivel 1 (autómata linealmente acotado, LBA)
 *  - acotada = false -> nivel 0 (Máquina de Turing con cinta extensible)
 *
 * Acepta cuando llega al estado de aceptación. Rechaza si no hay transición,
 * si el cabezal se sale de una cinta acotada o si pasa del límite de pasos
 * (una Máquina de Turing puede no detenerse nunca).
 */
public class MaquinaTuring extends Maquina {
    private static final int LIMITE_PASOS = 2000;

    private boolean acotada;
    private String estadoInicial;
    private String estadoAceptacion;
    private ArrayList<TransicionMT> transiciones = new ArrayList<>();

    public MaquinaTuring(String nombre, String descripcion, boolean acotada,
                         String estadoInicial, String estadoAceptacion) {
        super(nombre, descripcion);
        this.acotada = acotada;
        this.estadoInicial = estadoInicial;
        this.estadoAceptacion = estadoAceptacion;
    }

    public void agregarTransicion(String estado, char leido, char escribir, char movimiento, String siguiente) {
        transiciones.add(new TransicionMT(estado, leido, escribir, movimiento, siguiente));
    }

    private TransicionMT buscarTransicion(String estado, char leido) {
        for (TransicionMT t : transiciones) {
            if (t.getEstado().equals(estado) && t.getLeido() == leido) {
                return t;
            }
        }
        return null;
    }

    private String textoMovimiento(char movimiento) {
        if (movimiento == 'L') {
            return "izquierda";
        } else if (movimiento == 'R') {
            return "derecha";
        }
        return "quieto";
    }

    // Crea un paso con la foto actual de la cinta
    private Paso crearPaso(int numero, String estado, Cinta cinta, String descripcion) {
        Paso paso = new Paso(numero, estado, descripcion);
        paso.setCinta(cinta.getContenido());
        paso.setCabezal(cinta.getCabezal());
        return paso;
    }

    @Override
    public Resultado simular(String cadena) {
        Resultado resultado = new Resultado();
        Cinta cinta = new Cinta(cadena, acotada);
        String estado = estadoInicial;
        int numero = 0;

        resultado.agregarPaso(crearPaso(0, estado, cinta, "Configuración inicial en el estado " + estado));

        while (true) {
            if (estado.equals(estadoAceptacion)) {
                resultado.terminar(true, "Llegó al estado de aceptación " + estado);
                return resultado;
            }
            if (numero >= LIMITE_PASOS) {
                resultado.terminar(false, "Se superó el límite de " + LIMITE_PASOS + " pasos (posible ciclo infinito)");
                return resultado;
            }

            char leido = cinta.leer();
            TransicionMT t = buscarTransicion(estado, leido);
            if (t == null) {
                resultado.terminar(false, "No hay transición desde " + estado + " leyendo '" + leido + "'");
                return resultado;
            }

            if (!cinta.escribir(t.getEscribir())) {
                resultado.terminar(false, "La máquina intentó borrar un delimitador de la cinta acotada");
                return resultado;
            }
            if (!cinta.mover(t.getMovimiento())) {
                resultado.terminar(false, "El cabezal intentó salirse de la cinta acotada");
                return resultado;
            }

            estado = t.getSiguiente();
            numero++;
            String texto = "Lee '" + leido + "', escribe '" + t.getEscribir() + "', "
                    + textoMovimiento(t.getMovimiento()) + " → " + estado;
            resultado.agregarPaso(crearPaso(numero, estado, cinta, texto));
        }
    }

    /** Todos los símbolos que la máquina lee o escribe (sirve para mostrar el alfabeto de la cinta). */
    public String getAlfabetoCinta() {
        String alfabeto = "";
        if (acotada) {
            alfabeto = "" + Cinta.INICIO;
        }
        for (TransicionMT t : transiciones) {
            if (alfabeto.indexOf(t.getLeido()) < 0) {
                alfabeto = alfabeto + t.getLeido();
            }
            if (alfabeto.indexOf(t.getEscribir()) < 0) {
                alfabeto = alfabeto + t.getEscribir();
            }
        }
        if (acotada) {
            if (alfabeto.indexOf(Cinta.FIN) < 0) {
                alfabeto = alfabeto + Cinta.FIN;
            }
        } else if (alfabeto.indexOf(Cinta.BLANCO) < 0) {
            alfabeto = alfabeto + Cinta.BLANCO;
        }
        return alfabeto;
    }

    public boolean isAcotada() { return acotada; }
    public String getEstadoInicial() { return estadoInicial; }
    public ArrayList<TransicionMT> getTransiciones() { return transiciones; }
}
