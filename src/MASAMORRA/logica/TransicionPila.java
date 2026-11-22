package MASAMORRA.logica;

/**
 * δ(estado, simbolo, tope) = (nuevoEstado, apilar)
 *
 * "apilar" es lo que se escribe en la pila después de sacar el tope:
 *   ""   -> solo se saca el tope
 *   "A"  -> el tope se reemplaza por A
 *   "AZ" -> queda Z y encima A (el PRIMER carácter queda como nuevo tope)
 */
public class TransicionPila {
    private String estado;
    private char simbolo;       // símbolo de entrada (AutomataPila.EPSILON si no lee nada)
    private char tope;
    private String nuevoEstado;
    private String apilar;

    public TransicionPila(String estado, char simbolo, char tope, String nuevoEstado, String apilar) {
        this.estado = estado;
        this.simbolo = simbolo;
        this.tope = tope;
        this.nuevoEstado = nuevoEstado;
        this.apilar = apilar;
    }

    public String getEstado() { return estado; }
    public char getSimbolo() { return simbolo; }
    public char getTope() { return tope; }
    public String getNuevoEstado() { return nuevoEstado; }
    public String getApilar() { return apilar; }

    @Override
    public String toString() {
        String escribe = apilar;
        if (apilar.equals("")) {
            escribe = "ε";
        }
        return "δ(" + estado + ", " + simbolo + ", " + tope + ") = (" + nuevoEstado + ", " + escribe + ")";
    }
}
