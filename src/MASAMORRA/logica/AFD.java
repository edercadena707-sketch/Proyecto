package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * NIVEL 3 - Autómata Finito Determinista.
 * Para cada estado y cada símbolo hay a lo sumo una transición.
 * Si falta una transición, la cadena se rechaza.
 */
public class AFD extends Maquina {
    private String estadoInicial;
    private ArrayList<String> estados = new ArrayList<>();
    private ArrayList<String> estadosAceptacion = new ArrayList<>();
    private ArrayList<TransicionAFD> transiciones = new ArrayList<>();

    public AFD(String nombre, String descripcion, String estadoInicial) {
        super(nombre, descripcion);
        this.estadoInicial = estadoInicial;
        estados.add(estadoInicial);   // el inicial siempre es el primero de la lista
    }

    private void registrarEstado(String estado) {
        if (!estados.contains(estado)) {
            estados.add(estado);
        }
    }

    public void agregarTransicion(String desde, char simbolo, String hacia) {
        registrarEstado(desde);
        registrarEstado(hacia);
        transiciones.add(new TransicionAFD(desde, simbolo, hacia));
    }

    public void marcarComoAceptacion(String estado) {
        registrarEstado(estado);
        if (!estadosAceptacion.contains(estado)) {
            estadosAceptacion.add(estado);
        }
    }

    public boolean esAceptacion(String estado) {
        return estadosAceptacion.contains(estado);
    }

    // Busca la transición que sale de "estado" con ese símbolo (null si no existe)
    private TransicionAFD buscarTransicion(String estado, char simbolo) {
        for (TransicionAFD t : transiciones) {
            if (t.getDesde().equals(estado) && t.getSimbolo() == simbolo) {
                return t;
            }
        }
        return null;
    }

    @Override
    public Resultado simular(String cadena) {
        Resultado resultado = new Resultado();
        String estadoActual = estadoInicial;
        resultado.agregarPaso(new Paso(0, estadoActual, "Inicio en el estado " + estadoActual));

        for (int i = 0; i < cadena.length(); i++) {
            char simbolo = cadena.charAt(i);
            TransicionAFD t = buscarTransicion(estadoActual, simbolo);

            if (t == null) {
                resultado.terminar(false, "No hay transición desde " + estadoActual
                        + " leyendo '" + simbolo + "'");
                return resultado;
            }

            String anterior = estadoActual;
            estadoActual = t.getHacia();
            Paso paso = new Paso(i + 1, estadoActual,
                    "Lee '" + simbolo + "': " + anterior + " → " + estadoActual);
            paso.setEstadoAnterior(anterior);
            resultado.agregarPaso(paso);
        }

        if (esAceptacion(estadoActual)) {
            resultado.terminar(true, "Terminó la cadena en " + estadoActual + ", que es estado de aceptación");
        } else {
            resultado.terminar(false, "Terminó la cadena en " + estadoActual + ", que NO es estado de aceptación");
        }
        return resultado;
    }

    public String getEstadoInicial() { return estadoInicial; }
    public ArrayList<String> getEstados() { return estados; }
    public ArrayList<TransicionAFD> getTransiciones() { return transiciones; }
}
