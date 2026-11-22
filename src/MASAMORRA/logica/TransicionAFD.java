package MASAMORRA.logica;

/** Una flecha del autómata: desde --(simbolo)--> hacia */
public class TransicionAFD {
    private String desde;
    private char simbolo;
    private String hacia;

    public TransicionAFD(String desde, char simbolo, String hacia) {
        this.desde = desde;
        this.simbolo = simbolo;
        this.hacia = hacia;
    }

    public String getDesde() { return desde; }
    public char getSimbolo() { return simbolo; }
    public String getHacia() { return hacia; }
}
