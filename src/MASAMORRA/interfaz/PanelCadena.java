package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * Muestra la cadena de entrada. Cada símbolo tiene su color: los ya leídos están "encendidos",
 * el siguiente a leer lleva un anillo amarillo y los que faltan están apagados.
 */
public class PanelCadena extends JPanel {
    private String cadena = "";
    private int posicion = -1;     // -1 = todavía no se ha simulado
    private Color acento = Estilo.AZUL;

    public PanelCadena() {
        setBackground(Estilo.FONDO_DIBUJO);
        setPreferredSize(new Dimension(100, 112));
        setBorder(BorderFactory.createLineBorder(Estilo.BORDE, 2));
    }

    public void setAcento(Color acento) {
        this.acento = acento;
    }

    public void setCadena(String cadena, int posicion) {
        this.cadena = cadena;
        this.posicion = posicion;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Estilo.activarSuavizado(g2);
        g2.setPaint(new java.awt.GradientPaint(0, 0, Estilo.FONDO_DIBUJO, getWidth(), 0,
                Estilo.mezclar(Estilo.FONDO_DIBUJO, acento, 0.18)));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setFont(Estilo.NEGRITA);
        g2.setColor(acento);
        g2.drawString("CADENA DE ENTRADA", 14, 24);
        g2.setFont(Estilo.NORMAL.deriveFont(12f));
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("encendido = ya leído  ·  anillo amarillo = siguiente", 14, getHeight() - 12);

        if (cadena.length() == 0) {
            g2.setFont(Estilo.NORMAL);
            g2.setColor(Estilo.TEXTO);
            g2.drawString("(cadena vacía)", 14, 56);
            return;
        }

        int tam = Math.min(38, (getWidth() - 28) / cadena.length());
        if (tam < 10) {
            tam = 10;
        }
        int y = 36;
        for (int i = 0; i < cadena.length(); i++) {
            int x = 14 + i * tam;
            Color color = Estilo.colorSimbolo(cadena.charAt(i));
            boolean leido = posicion >= 0 && i < posicion;
            boolean siguiente = (i == posicion);
            boolean sinSimular = (posicion < 0);

            Color relleno = color;
            if (!leido && !siguiente && !sinSimular) {
                relleno = Estilo.mezclar(color, Estilo.FONDO_DIBUJO, 0.78);    // apagado
            } else if (sinSimular) {
                relleno = Estilo.mezclar(color, Estilo.FONDO_DIBUJO, 0.45);
            }
            if (siguiente) {
                Estilo.pintarBrillo(g2, x + tam / 2.0, y + 21, tam / 2.0 - 2, Estilo.ACTIVO);
            }
            g2.setColor(relleno);
            g2.fillRoundRect(x + 2, y, tam - 4, 42, 10, 10);
            if (siguiente) {
                g2.setColor(Estilo.ACTIVO);
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawRoundRect(x + 2, y, tam - 4, 42, 10, 10);
            }
            if (tam >= 16) {
                g2.setFont(Estilo.SIMBOLO);
                g2.setColor(Estilo.FONDO);
                String simbolo = "" + cadena.charAt(i);
                int ancho = g2.getFontMetrics().stringWidth(simbolo);
                g2.drawString(simbolo, x + (tam - ancho) / 2, y + 28);
            }
        }
    }
}
