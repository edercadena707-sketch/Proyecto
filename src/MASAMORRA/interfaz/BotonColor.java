package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

/** Botón con degradado, esquinas redondeadas y efecto al pasar el mouse. */
public class BotonColor extends JButton {
    private Color color;

    public BotonColor(String texto, Color color) {
        super(texto);
        this.color = color;
        setFont(Estilo.NEGRITA);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(getText()) + 36, 36);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        int ancho = getWidth();
        int alto = getHeight();

        Color base = color;
        Color colorTexto = Estilo.FONDO;
        if (!isEnabled()) {
            base = Estilo.DESHABILITADO;
            colorTexto = Estilo.TEXTO_SUAVE;
        } else if (getModel().isPressed()) {
            base = Estilo.mezclar(color, Color.BLACK, 0.25);
        } else if (getModel().isRollover()) {
            base = Estilo.mezclar(color, Color.WHITE, 0.25);
        }

        // sombra
        if (isEnabled()) {
            g2.setColor(new Color(0, 0, 0, 70));
            g2.fillRoundRect(0, 3, ancho, alto - 3, 18, 18);
        }
        // cuerpo con degradado
        g2.setPaint(new GradientPaint(0, 0, Estilo.mezclar(base, Color.WHITE, 0.2),
                0, alto, Estilo.mezclar(base, Color.BLACK, 0.12)));
        g2.fillRoundRect(0, 0, ancho, alto - 3, 18, 18);

        // texto centrado
        g2.setColor(colorTexto);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(getText(), (ancho - fm.stringWidth(getText())) / 2, (alto - 3 + fm.getAscent()) / 2 - 2);
        g2.dispose();
    }
}
