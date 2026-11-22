package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

/** Botón del menú lateral: círculo de color con el número del nivel, título y subtítulo. */
public class BotonMenu extends JButton {
    private String numero;
    private String titulo;
    private String subtitulo;
    private Color acento;
    private boolean seleccionado = false;

    public BotonMenu(String numero, String titulo, String subtitulo, Color acento) {
        this.numero = numero;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.acento = acento;
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setPreferredSize(new Dimension(212, 50));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        int ancho = getWidth();
        int alto = getHeight();
        boolean encima = getModel().isRollover();

        // fondo
        if (seleccionado) {
            g2.setPaint(new GradientPaint(0, 0, Estilo.mezclar(acento, Color.BLACK, 0.55),
                    ancho, 0, Estilo.mezclar(acento, Color.BLACK, 0.2)));
        } else if (encima) {
            g2.setColor(Estilo.TARJETA_CLARA);
        } else {
            g2.setColor(new Color(255, 255, 255, 14));
        }
        g2.fillRoundRect(0, 0, ancho - 1, alto - 1, 20, 20);

        // borde
        if (seleccionado || encima) {
            g2.setColor(acento);
            g2.setStroke(new BasicStroke(2f));
            g2.drawRoundRect(1, 1, ancho - 3, alto - 3, 20, 20);
        }

        // círculo con el número
        int tam = 34;
        int x = 11;
        int y = (alto - tam) / 2;
        g2.setColor(acento);
        g2.fillOval(x, y, tam, tam);
        g2.setColor(Estilo.FONDO);
        g2.setFont(Estilo.GRANDE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(numero, x + (tam - fm.stringWidth(numero)) / 2, y + (tam + fm.getAscent()) / 2 - 3);

        // textos
        g2.setColor(Color.WHITE);
        g2.setFont(Estilo.NEGRITA);
        g2.drawString(titulo, 55, alto / 2 - 2);
        if (seleccionado) {
            g2.setColor(Estilo.TEXTO);
        } else {
            g2.setColor(Estilo.TEXTO_SUAVE);
        }
        g2.setFont(Estilo.NORMAL.deriveFont(12f));
        g2.drawString(subtitulo, 55, alto / 2 + 14);
        g2.dispose();
    }
}
