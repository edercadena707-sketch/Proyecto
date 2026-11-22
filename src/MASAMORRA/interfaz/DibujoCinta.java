package MASAMORRA.interfaz;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import MASAMORRA.logica.Cinta;
import MASAMORRA.logica.MaquinaTuring;

/** Dibuja la cinta de la Máquina de Turing, el cabezal, el estado activo y el alfabeto de la cinta. */
public class DibujoCinta extends PanelDibujo {
    private static final int TAM = 56;   // tamaño de cada celda

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (maquina == null) {
            return;
        }
        MaquinaTuring mt = (MaquinaTuring) maquina;
        Graphics2D g2 = (Graphics2D) g;
        int ancho = getWidth();
        int alto = getHeight();

        // ----- información fija: alfabeto de la cinta (cada símbolo con su color) -----
        String alfabeto = mt.getAlfabetoCinta();
        g2.setFont(Estilo.NEGRITA);
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("Alfabeto de la cinta:", 16, alto - 44);
        int xChip = 175;
        for (int i = 0; i < alfabeto.length(); i++) {
            Color color = Estilo.colorSimbolo(alfabeto.charAt(i));
            if (alfabeto.charAt(i) == '_') {
                color = Estilo.GRIS;
            }
            g2.setColor(color);
            g2.fillRoundRect(xChip, alto - 62, 28, 26, 10, 10);
            g2.setColor(Estilo.FONDO);
            g2.setFont(Estilo.SIMBOLO.deriveFont(16f));
            String s = "" + alfabeto.charAt(i);
            g2.drawString(s, xChip + (28 - g2.getFontMetrics().stringWidth(s)) / 2, alto - 43);
            xChip += 34;
        }
        g2.setFont(Estilo.NORMAL.deriveFont(12f));
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("( _ = blanco )", xChip + 6, alto - 43);
        if (mt.isAcotada()) {
            g2.drawString("Cinta ACOTADA: la entrada va entre [ y ]; el cabezal no puede salirse.", 16, alto - 14);
        } else {
            g2.drawString("Cinta EXTENSIBLE: crece con blancos (celdas punteadas).", 16, alto - 14);
        }

        if (paso == null) {
            g2.setFont(Estilo.GRANDE);
            g2.drawString("Elige un ejemplo y presiona Simular", 16, 32);
            return;
        }

        // ----- estado activo -----
        g2.setFont(Estilo.GRANDE);
        g2.setColor(Estilo.TEXTO_SUAVE);
        g2.drawString("Estado activo:", 16, 34);
        FontMetrics fm = g2.getFontMetrics();
        int anchoEstado = Math.max(70, fm.stringWidth(paso.getEstado()) + 30);
        Estilo.pintarBrillo(g2, 170 + anchoEstado / 2.0, 24, 14, Estilo.ACTIVO);
        g2.setPaint(new GradientPaint(0, 6, Color.WHITE, 0, 42, Estilo.ACTIVO));
        g2.fillRoundRect(170, 8, anchoEstado, 34, 16, 16);
        g2.setColor(Estilo.FONDO);
        g2.drawString(paso.getEstado(), 170 + (anchoEstado - fm.stringWidth(paso.getEstado())) / 2, 33);

        // ----- qué celdas se pueden mostrar -----
        String cinta = paso.getCinta();
        int cabezal = paso.getCabezal();
        int primera = 0;
        int ultima = cinta.length() - 1;
        if (!mt.isAcotada()) {       // en la cinta extensible se muestra un blanco virtual a cada lado
            primera = -1;
            ultima = cinta.length();
        }
        int total = ultima - primera + 1;
        int visibles = (ancho - 30) / TAM;
        int inicio = primera;
        int mostrar = total;
        if (total > visibles) {      // la cinta no cabe: se muestra una ventana alrededor del cabezal
            mostrar = visibles;
            inicio = cabezal - visibles / 2;
            if (inicio < primera) {
                inicio = primera;
            }
            if (inicio > ultima - visibles + 1) {
                inicio = ultima - visibles + 1;
            }
        }
        int x0 = (ancho - mostrar * TAM) / 2;
        int yCinta = alto / 2 - 14;

        // ----- celdas -----
        for (int j = 0; j < mostrar; j++) {
            int indice = inicio + j;
            int x = x0 + j * TAM;

            if (indice < 0 || indice >= cinta.length()) {      // blanco virtual (cinta extensible)
                g2.setColor(Estilo.BORDE);
                g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
                        new float[] {4f, 4f}, 0f));
                g2.drawRoundRect(x + 2, yCinta, TAM - 4, TAM, 10, 10);
                g2.setFont(Estilo.SIMBOLO);
                g2.drawString("_", x + TAM / 2 - 5, yCinta + TAM / 2 + 7);
                continue;
            }

            char c = cinta.charAt(indice);
            Color color = Estilo.colorSimbolo(c);
            if (c == Cinta.BLANCO) {
                color = Estilo.TARJETA_CLARA;
            }
            boolean esCabezal = (indice == cabezal);

            if (esCabezal) {
                Estilo.pintarBrillo(g2, x + TAM / 2.0, yCinta + TAM / 2.0, TAM / 2.0 - 2, Estilo.ACTIVO);
            }
            g2.setPaint(new GradientPaint(x, yCinta, Estilo.mezclar(color, Color.WHITE, 0.25),
                    x, yCinta + TAM, Estilo.mezclar(color, Color.BLACK, 0.18)));
            g2.fillRoundRect(x + 2, yCinta, TAM - 4, TAM, 10, 10);
            if (esCabezal) {
                g2.setColor(Estilo.ACTIVO);
                g2.setStroke(new BasicStroke(4f));
            } else {
                g2.setColor(Estilo.conAlfa(Color.WHITE, 70));
                g2.setStroke(new BasicStroke(1.4f));
            }
            g2.drawRoundRect(x + 2, yCinta, TAM - 4, TAM, 10, 10);

            g2.setFont(Estilo.SIMBOLO.deriveFont(22f));
            if (c == Cinta.BLANCO) {
                g2.setColor(Estilo.TEXTO_SUAVE);
            } else {
                g2.setColor(Estilo.FONDO);
            }
            String simbolo = "" + c;
            g2.drawString(simbolo, x + (TAM - g2.getFontMetrics().stringWidth(simbolo)) / 2, yCinta + TAM / 2 + 8);

            if (esCabezal) {      // el cabezal: un triángulo que apunta a la celda
                int cx = x + TAM / 2;
                Polygon triangulo = new Polygon();
                triangulo.addPoint(cx, yCinta - 5);
                triangulo.addPoint(cx - 12, yCinta - 26);
                triangulo.addPoint(cx + 12, yCinta - 26);
                g2.setColor(Estilo.ACTIVO);
                g2.fillPolygon(triangulo);
                g2.setFont(Estilo.NEGRITA);
                g2.drawString("cabezal", cx - 27, yCinta - 33);

                g2.setColor(Estilo.TEXTO);
                String etiqueta = "Símbolo bajo el cabezal:  ";
                g2.drawString(etiqueta, x0, yCinta + TAM + 36);
                int anchoEtiqueta = g2.getFontMetrics().stringWidth(etiqueta);
                g2.setColor(Estilo.ACTIVO);
                g2.setFont(Estilo.SIMBOLO);
                g2.drawString("'" + c + "'", x0 + anchoEtiqueta, yCinta + TAM + 36);
            }
        }
    }
}
