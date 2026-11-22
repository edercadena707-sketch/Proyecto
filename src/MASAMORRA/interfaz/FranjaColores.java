package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/** Una franja con los cuatro colores de los niveles (decoración del menú). */
public class FranjaColores extends JPanel {
    private Color[] colores = {Estilo.NIVEL3, Estilo.NIVEL2, Estilo.NIVEL1, Estilo.NIVEL0};

    public FranjaColores() {
        setOpaque(false);
        setPreferredSize(new Dimension(100, 6));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        int ancho = (getWidth() - 3 * 6) / colores.length;
        for (int i = 0; i < colores.length; i++) {
            g2.setColor(colores[i]);
            g2.fillRoundRect(i * (ancho + 6), 0, ancho, getHeight(), 8, 8);
        }
        g2.dispose();
    }
}
