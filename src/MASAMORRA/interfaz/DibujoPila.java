package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import MASAMORRA.logica.AutomataPila;

/** Dibuja el estado actual (izquierda) y la pila como cajas de colores apiladas (derecha). */
public class DibujoPila extends PanelDibujo {

    // El fondo de la pila (Z) es gris; los demás símbolos usan su color
    private Color colorCaja(char simbolo) {
        if (simbolo == AutomataPila.FONDO) {
            return Estilo.GRIS;
        }
        return Estilo.colorSimbolo(simbolo);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int ancho = getWidth();
        int alto = getHeight();

        g2.setFont(Estilo.GRANDE);
        if (paso == null) {
            g2.setColor(Estilo.TEXTO_SUAVE);
            g2.drawString("Elige un ejemplo y presiona Simular", 16, 32);
            return;
        }
        g2.setColor(acento);
        g2.drawString("AUTÓMATA DE PILA", 16, 32);

        // ----- estado actual (con brillo) -----
        int cx = ancho / 4;
        int cy = alto / 2;
        g2.setFont(Estilo.NEGRITA);
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("ESTADO ACTUAL", cx - 56, cy - 70);
        Estilo.pintarBrillo(g2, cx, cy, 38, Estilo.ACTIVO);
        Ellipse2D circulo = new Ellipse2D.Double(cx - 38, cy - 38, 76, 76);
        g2.setPaint(new GradientPaint(cx, cy - 38, Color.WHITE, cx, cy + 38, Estilo.ACTIVO));
        g2.fill(circulo);
        g2.setColor(Estilo.ACTIVO);
        g2.setStroke(new BasicStroke(3f));
        g2.draw(circulo);
        g2.setFont(Estilo.GRANDE);
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Estilo.FONDO);
        g2.drawString(paso.getEstado(), cx - fm.stringWidth(paso.getEstado()) / 2, cy + 6);

        // ----- pila -----
        String pila = paso.getPila();
        int anchoCaja = 150;
        int xCaja = ancho * 5 / 8 - anchoCaja / 2;
        int base = alto - 55;                 // y donde está el fondo
        int disponible = base - 60;
        int altoCaja = 40;
        if (pila.length() * altoCaja > disponible) {
            altoCaja = Math.max(10, disponible / pila.length());
        }

        g2.setFont(Estilo.NEGRITA);
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("PILA", xCaja + anchoCaja / 2 - 16, 66);

        // recipiente tenue donde se apilan las cajas
        g2.setColor(new Color(255, 255, 255, 12));
        g2.fillRoundRect(xCaja - 14, 78, anchoCaja + 28, base - 74, 18, 18);
        g2.setColor(Estilo.conAlfa(acento, 90));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(xCaja - 14, 78, anchoCaja + 28, base - 74, 18, 18);

        for (int i = 0; i < pila.length(); i++) {
            int y = base - (i + 1) * altoCaja;
            boolean esTope = (i == pila.length() - 1);
            Color color = colorCaja(pila.charAt(i));

            if (esTope) {   // brillo amarillo detrás del tope
                g2.setColor(Estilo.conAlfa(Estilo.ACTIVO, 45));
                g2.fillRoundRect(xCaja - 8, y - 5, anchoCaja + 16, altoCaja + 10, 16, 16);
            }
            g2.setPaint(new GradientPaint(xCaja, y, Estilo.mezclar(color, Color.WHITE, 0.3),
                    xCaja, y + altoCaja, Estilo.mezclar(color, Color.BLACK, 0.15)));
            g2.fillRoundRect(xCaja, y + 1, anchoCaja, altoCaja - 2, 12, 12);
            if (esTope) {
                g2.setColor(Estilo.ACTIVO);
                g2.setStroke(new BasicStroke(3.5f));
            } else {
                g2.setColor(Estilo.conAlfa(Color.WHITE, 90));
                g2.setStroke(new BasicStroke(1.5f));
            }
            g2.drawRoundRect(xCaja, y + 1, anchoCaja, altoCaja - 2, 12, 12);

            if (altoCaja >= 18) {
                g2.setFont(Estilo.SIMBOLO.deriveFont(22f));
                g2.setColor(Estilo.FONDO);
                String simbolo = "" + pila.charAt(i);
                int anchoSimbolo = g2.getFontMetrics().stringWidth(simbolo);
                g2.drawString(simbolo, xCaja + (anchoCaja - anchoSimbolo) / 2, y + altoCaja / 2 + 8);
            }
            if (esTope) {
                g2.setFont(Estilo.NEGRITA);
                g2.setColor(Estilo.ACTIVO);
                g2.drawString("tope  →", xCaja - 82, y + altoCaja / 2 + 5);
            }
        }
        if (pila.length() == 0) {
            g2.setFont(Estilo.NORMAL);
            g2.setColor(Estilo.TEXTO_SUAVE);
            g2.drawString("(pila vacía)", xCaja + 35, base - 18);
        }

        g2.setStroke(new BasicStroke(4f));
        g2.setColor(acento);
        g2.drawLine(xCaja - 18, base + 2, xCaja + anchoCaja + 18, base + 2);
        g2.setFont(Estilo.NORMAL);
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("fondo", xCaja + anchoCaja / 2 - 18, base + 24);
    }
}
