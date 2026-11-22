package MASAMORRA.logica;

/**
 * Una "foto" de la máquina en un momento de la simulación.
 * Cada máquina llena solo los datos que le sirven:
 *  - AFD: posición y estado
 *  - Autómata de pila: posición, estado y pila
 *  - Máquina de Turing: estado, cinta y cabezal
 */
public class Paso {
    private int posicion;        // cuántos símbolos de la entrada ya se leyeron
    private String estado;       // estado actual
    private String pila = "";    // el primer carácter es el fondo y el último es el tope
    private String cinta = "";   // contenido de la cinta, una celda por carácter
    private int cabezal = 0;     // índice de la celda donde está el cabezal
    private String estadoAnterior = "";   // estado antes de este paso (el AFD lo usa para resaltar la flecha)
    private String descripcion;  // texto que explica qué pasó en este paso

    public Paso(int posicion, String estado, String descripcion) {
        this.posicion = posicion;
        this.estado = estado;
        this.descripcion = descripcion;
    }

    public int getPosicion() { return posicion; }
    public String getEstado() { return estado; }
    public String getPila() { return pila; }
    public String getCinta() { return cinta; }
    public int getCabezal() { return cabezal; }
    public String getDescripcion() { return descripcion; }
    public String getEstadoAnterior() { return estadoAnterior; }

    public void setPila(String pila) { this.pila = pila; }
    public void setCinta(String cinta) { this.cinta = cinta; }
    public void setCabezal(int cabezal) { this.cabezal = cabezal; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }
}
