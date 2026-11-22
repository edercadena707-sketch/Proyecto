package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Ellipse2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

import MASAMORRA.logica.AFD;
import MASAMORRA.logica.TransicionAFD;

/**
 * Dibuja el diagrama de estados del AFD.
 * Los estados se ubican en círculo; el estado actual brilla en amarillo
 * y la flecha que se acaba de usar también se resalta.
 */
public class DibujoAFD extends PanelDibujo {
    private int R = 27;   // radio de cada estado (cambia según el espacio y la cantidad de estados)
    private static final Color COLOR_FLECHA = new Color(135, 146, 212);

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maquina == null) {
            return;
        }
        AFD afd = (AFD) maquina;
        Graphics2D g2 = (Graphics2D) g;

        ArrayList<String> estados = afd.getEstados();
        int n = estados.size();

        // estados más chicos si son muchos, y más grandes si sobra espacio
        R = 27;
        if (n >= 7) {
            R = 23;
        } else if (getHeight() > 480) {
            R = 32;
        }

        // ----- posición de cada estado -----
        // Se reparten en una elipse que aprovecha todo el espacio del panel. El primer estado queda a la
        // izquierda y se dejan márgenes para el texto de arriba, la flecha de entrada y los bucles.
        int margenIzq = R + 60;
        int margenDer = R + 25;
        int margenSup = R + 52;
        int margenInf = R + 30;
        double anchoUtil = getWidth() - margenIzq - margenDer;
        double radioY = (getHeight() - margenSup - margenInf) / 2.0;
        if (radioY < 45) {
            radioY = 45;
        }
        double radioX = anchoUtil / 2.0;
        if (radioX > radioY * 2.1) {
            radioX = radioY * 2.1;       // que la elipse no quede demasiado aplastada
        }
        if (radioX > 60 + 45 * n) {
            radioX = 60 + 45 * n;        // con pocos estados no hace falta separarlos tanto
        }
        double centroX = margenIzq + anchoUtil / 2.0;
        double centroY = margenSup + radioY;

        double[] xs = new double[n];
        double[] ys = new double[n];
        for (int i = 0; i < n; i++) {
            if (n == 1) {
                xs[i] = centroX;
                ys[i] = centroY;
            } else {
                double angulo = Math.PI + 2 * Math.PI * i / n;
                xs[i] = centroX + radioX * Math.cos(angulo);
                ys[i] = centroY + radioY * Math.sin(angulo);
            }
        }

        // ----- flechas (si dos transiciones van entre los mismos estados, se juntan en una flecha) -----
        g2.setFont(Estilo.NEGRITA);
        ArrayList<String> dibujadas = new ArrayList<>();
        for (TransicionAFD t : afd.getTransiciones()) {
            String clave = t.getDesde() + ">" + t.getHacia();
            if (dibujadas.contains(clave)) {
                continue;
            }
            dibujadas.add(clave);

            String etiqueta = "";
            for (TransicionAFD u : afd.getTransiciones()) {
                if (u.getDesde().equals(t.getDesde()) && u.getHacia().equals(t.getHacia())) {
                    if (!etiqueta.equals("")) {
                        etiqueta = etiqueta + ",";
                    }
                    etiqueta = etiqueta + u.getSimbolo();
                }
            }

            // la flecha que se acaba de usar se resalta
            boolean resaltada = paso != null && !paso.getEstadoAnterior().equals("")
                    && t.getDesde().equals(paso.getEstadoAnterior()) && t.getHacia().equals(paso.getEstado());

            int i = estados.indexOf(t.getDesde());
            int j = estados.indexOf(t.getHacia());
            if (i == j) {
                dibujarBucle(g2, xs[i], ys[i], etiqueta, resaltada);
            } else {
                boolean hayVuelta = existeTransicion(afd, t.getHacia(), t.getDesde());
                dibujarFlecha(g2, xs[i], ys[i], xs[j], ys[j], etiqueta, hayVuelta, resaltada);
            }
        }

        // ----- flecha de entrada al estado inicial -----
        g2.setColor(acento);
        g2.setStroke(new BasicStroke(3f));
        g2.drawLine((int) (xs[0] - R - 50), (int) ys[0], (int) (xs[0] - R - 11), (int) ys[0]);
        dibujarPunta(g2, xs[0] - R, ys[0], 1, 0);

        // ----- estados -----
        for (int i = 0; i < n; i++) {
            String nombre = estados.get(i);
            boolean actual = paso != null && paso.getEstado().equals(nombre);
            Ellipse2D circulo = new Ellipse2D.Double(xs[i] - R, ys[i] - R, 2 * R, 2 * R);

            if (actual) {
                Estilo.pintarBrillo(g2, xs[i], ys[i], R, Estilo.ACTIVO);
                g2.setPaint(new GradientPaint((float) xs[i], (float) (ys[i] - R), Color.WHITE,
                        (float) xs[i], (float) (ys[i] + R), Estilo.ACTIVO));
            } else {
                g2.setPaint(new GradientPaint((float) xs[i], (float) (ys[i] - R), Estilo.TARJETA_CLARA,
                        (float) xs[i], (float) (ys[i] + R), Estilo.TARJETA));
            }
            g2.fill(circulo);

            if (actual) {
                g2.setColor(Estilo.ACTIVO);
            } else {
                g2.setColor(acento);
            }
            g2.setStroke(new BasicStroke(3f));
            g2.draw(circulo);
            if (afd.esAceptacion(nombre)) {
                g2.setStroke(new BasicStroke(2f));
                g2.draw(new Ellipse2D.Double(xs[i] - R + 6, ys[i] - R + 6, 2 * R - 12, 2 * R - 12));
            }

            g2.setFont(Estilo.NEGRITA);
            FontMetrics fm = g2.getFontMetrics();
            if (actual) {
                g2.setColor(Estilo.FONDO);
            } else {
                g2.setColor(Estilo.TEXTO);
            }
            g2.drawString(nombre, (int) (xs[i] - fm.stringWidth(nombre) / 2.0), (int) (ys[i] + 5));
        }

        // ----- textos -----
        g2.setFont(Estilo.GRANDE);
        if (paso != null) {
            g2.setColor(Estilo.TEXTO_SUAVE);
            g2.drawString("Estado actual:", 16, 32);
            int anchoTexto = g2.getFontMetrics().stringWidth("Estado actual:  ");
            g2.setColor(Estilo.ACTIVO);
            g2.drawString(paso.getEstado(), 16 + anchoTexto, 32);
        } else {
            g2.setColor(Estilo.TEXTO_SUAVE);
            g2.drawString("Elige un ejemplo y presiona Simular", 16, 32);
        }
        g2.setFont(Estilo.NORMAL.deriveFont(12f));
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("Flecha de entrada = estado inicial   |   Doble círculo = aceptación   |   Amarillo = estado actual y flecha recién usada",
                16, getHeight() - 12);
    }

    private boolean existeTransicion(AFD afd, String desde, String hacia) {
        for (TransicionAFD t : afd.getTransiciones()) {
            if (t.getDesde().equals(desde) && t.getHacia().equals(hacia)) {
                return true;
            }
        }
        return false;
    }

    // Triángulo con la punta en (x, y) apuntando en la dirección (dx, dy), que debe ser un vector de largo 1
    private void dibujarPunta(Graphics2D g2, double x, double y, double dx, double dy) {
        double px = -dy;
        double py = dx;
        Polygon p = new Polygon();
        p.addPoint((int) Math.round(x), (int) Math.round(y));
        p.addPoint((int) Math.round(x - dx * 12 + px * 6), (int) Math.round(y - dy * 12 + py * 6));
        p.addPoint((int) Math.round(x - dx * 12 - px * 6), (int) Math.round(y - dy * 12 - py * 6));
        g2.fill(p);
    }

    // Flecha entre dos estados distintos. Si hay flecha de vuelta, se curva para que no se encimen.
    private void dibujarFlecha(Graphics2D g2, double x1, double y1, double x2, double y2,
                               String etiqueta, boolean curva, boolean resaltada) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double largo = Math.sqrt(dx * dx + dy * dy);
        double ux = dx / largo;
        double uy = dy / largo;
        double px = -uy;   // perpendicular
        double py = ux;

        double desvio = 0;
        if (curva) {
            desvio = 44;
        }
        double cx = (x1 + x2) / 2 + px * desvio;   // punto de control de la curva
        double cy = (y1 + y2) / 2 + py * desvio;

        // dirección de salida y de llegada, para que la flecha empiece y termine en el borde de los círculos
        double sx = cx - x1;
        double sy = cy - y1;
        double ls = Math.sqrt(sx * sx + sy * sy);
        sx = sx / ls;
        sy = sy / ls;
        double ex = x2 - cx;
        double ey = y2 - cy;
        double le = Math.sqrt(ex * ex + ey * ey);
        ex = ex / le;
        ey = ey / le;

        double inicioX = x1 + sx * R;
        double inicioY = y1 + sy * R;
        double finX = x2 - ex * R;
        double finY = y2 - ey * R;

        if (resaltada) {
            g2.setColor(Estilo.ACTIVO);
            g2.setStroke(new BasicStroke(3.6f));
        } else {
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(1.9f));
        }
        g2.draw(new QuadCurve2D.Double(inicioX, inicioY, cx, cy, finX, finY));
        dibujarPunta(g2, finX, finY, ex, ey);

        // la etiqueta va cerca del estado de salida (a 35% del camino), así no se juntan todas en el centro
        double t = 0.35;
        double etiquetaX = (1 - t) * (1 - t) * inicioX + 2 * t * (1 - t) * cx + t * t * finX;
        double etiquetaY = (1 - t) * (1 - t) * inicioY + 2 * t * (1 - t) * cy + t * t * finY;
        escribirEtiqueta(g2, etiqueta, etiquetaX + px * 15, etiquetaY + py * 15, resaltada);
    }

    // Flecha de un estado hacia sí mismo: un circulito arriba del estado
    private void dibujarBucle(Graphics2D g2, double x, double y, String etiqueta, boolean resaltada) {
        double r = 15;
        if (resaltada) {
            g2.setColor(Estilo.ACTIVO);
            g2.setStroke(new BasicStroke(3.6f));
        } else {
            g2.setColor(COLOR_FLECHA);
            g2.setStroke(new BasicStroke(1.9f));
        }
        g2.draw(new Ellipse2D.Double(x - r, y - R - 2 * r + 3, 2 * r, 2 * r));
        dibujarPunta(g2, x + 9, y - R + 1, 0, 1);
        escribirEtiqueta(g2, etiqueta, x, y - R - 2 * r - 10, resaltada);
    }

    // El texto de la flecha va dentro de una pastillita de color
    private void escribirEtiqueta(Graphics2D g2, String texto, double x, double y, boolean resaltada) {
        g2.setFont(Estilo.NEGRITA);
        FontMetrics fm = g2.getFontMetrics();
        int ancho = fm.stringWidth(texto) + 12;
        int alto = fm.getAscent() + 6;
        int px = (int) (x - ancho / 2.0);
        int py = (int) (y - alto / 2.0);
        if (resaltada) {
            g2.setColor(Estilo.ACTIVO);
        } else {
            g2.setColor(Estilo.TARJETA);
        }
        g2.fillRoundRect(px, py, ancho, alto, 12, 12);
        if (resaltada) {
            g2.setColor(Estilo.FONDO);
        } else {
            g2.setColor(acento);
            g2.setStroke(new BasicStroke(1.4f));
            g2.drawRoundRect(px, py, ancho, alto, 12, 12);
        }
        g2.drawString(texto, px + 6, py + alto - 5);
    }
}
