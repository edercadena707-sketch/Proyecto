package MASAMORRA.logica;

import java.util.ArrayList;

/**
 * Lo que devuelve toda simulación: la lista de pasos (la "traza"),
 * si la cadena fue aceptada y el motivo.
 */
public class Resultado {
    private ArrayList<Paso> pasos = new ArrayList<>();
    private boolean aceptada = false;
    private String motivo = "";

    public void agregarPaso(Paso paso) {
        pasos.add(paso);
    }

    public void terminar(boolean aceptada, String motivo) {
        this.aceptada = aceptada;
        this.motivo = motivo;
    }

    public ArrayList<Paso> getPasos() { return pasos; }
    public boolean isAceptada() { return aceptada; }
    public String getMotivo() { return motivo; }
}
