package MASAMORRA.logica;

/** δ(estado, leido) = (siguiente, escribir, movimiento). El movimiento es 'L', 'R' o 'S'. */
public class TransicionMT {
    private String estado;
    private char leido;
    private char escribir;
    private char movimiento;
    private String siguiente;

    public TransicionMT(String estado, char leido, char escribir, char movimiento, String siguiente) {
        this.estado = estado;
        this.leido = leido;
        this.escribir = escribir;
        this.movimiento = movimiento;
        this.siguiente = siguiente;
    }

    public String getEstado() { return estado; }
    public char getLeido() { return leido; }
    public char getEscribir() { return escribir; }
    public char getMovimiento() { return movimiento; }
    public String getSiguiente() { return siguiente; }

    @Override
    public String toString() {
        return "δ(" + estado + ", " + leido + ") = (" + siguiente + ", " + escribir + ", " + movimiento + ")";
    }
}
