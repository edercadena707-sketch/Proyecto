package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/** Tarjeta con esquinas redondeadas y borde de color. */
public class PanelRedondeado extends JPanel {
    private Color fondo;
    private Color borde;

    public PanelRedondeado(Color fondo, Color borde) {
        this.fondo = fondo;
        this.borde = borde;
        setOpaque(false);
    }

    public void setColores(Color fondo, Color borde) {
        this.fondo = fondo;
        this.borde = borde;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        g2.setColor(fondo);
        g2.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
        g2.setColor(borde);
        g2.setStroke(new BasicStroke(1.8f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 22, 22);
        g2.dispose();
    }
}
