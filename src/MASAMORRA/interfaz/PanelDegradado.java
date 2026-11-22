package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Panel con fondo de degradado (de un color a otro). Se usa para el menú lateral
 * y para las cabeceras de cada nivel, que además pueden llevar una insignia con el número del nivel.
 */
public class PanelDegradado extends JPanel {
    private Color color1;
    private Color color2;
    private String insignia;         // número del nivel (puede ser null)
    private boolean vertical = false;
    private boolean adornos = true;
    private int redondeo = 24;

    public PanelDegradado(Color color1, Color color2, String insignia) {
        this.color1 = color1;
        this.color2 = color2;
        this.insignia = insignia;
        setOpaque(false);
    }

    public void setVertical(boolean vertical) { this.vertical = vertical; }
    public void setAdornos(boolean adornos) { this.adornos = adornos; }
    public void setRedondeo(int redondeo) { this.redondeo = redondeo; }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        int ancho = getWidth();
        int alto = getHeight();

        if (vertical) {
            g2.setPaint(new GradientPaint(0, 0, color1, 0, alto, color2));
        } else {
            g2.setPaint(new GradientPaint(0, 0, color1, ancho, 0, color2));
        }
        g2.fillRoundRect(0, 0, ancho, alto, redondeo, redondeo);

        if (adornos) {   // círculos translúcidos a la derecha, como burbujas
            g2.setColor(new Color(255, 255, 255, 26));
            g2.fillOval(ancho - 190, -70, 240, 240);
            g2.setColor(new Color(255, 255, 255, 20));
            g2.fillOval(ancho - 90, 28, 130, 130);
        }

        if (insignia != null) {
            int tam = Math.min(58, alto - 14);
            int x = 18;
            int y = (alto - tam) / 2;
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillOval(x + 2, y + 4, tam, tam);
            g2.setColor(Color.WHITE);
            g2.fillOval(x, y, tam, tam);
            g2.setColor(color1);
            g2.setFont(Estilo.TITULO.deriveFont(tam * 0.55f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(insignia, x + (tam - fm.stringWidth(insignia)) / 2, y + (tam + fm.getAscent()) / 2 - 4);
        }
        g2.dispose();
    }
}
