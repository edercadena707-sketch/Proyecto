package MASAMORRA;

import javax.swing.SwingUtilities;

import MASAMORRA.interfaz.Estilo;
import MASAMORRA.interfaz.VentanaPrincipal;

/** Punto de entrada del programa: abre la ventana principal. */
public class Main {
    public static void main(String[] args) {
        Estilo.aplicarTema();   // colores de los menús desplegables

        // invokeLater hace que la ventana se cree en el hilo de Swing (buena práctica)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }
}
