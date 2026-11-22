package MASAMORRA.interfaz;

import java.awt.Dimension;

import javax.swing.JFrame;

/** La ventana del programa. Todo su contenido está en PanelPrincipal. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        setTitle("Simulador de la Jerarquía de Chomsky");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1320, 800);
        setMinimumSize(new Dimension(1200, 680));
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);   // arranca ocupando toda la pantalla
        setContentPane(new PanelPrincipal());
    }
}
