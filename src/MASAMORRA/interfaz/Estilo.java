package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/** Colores, fuentes y pequeñas herramientas de dibujo que usa toda la interfaz (tema oscuro con colores vivos). */
public class Estilo {
    // ----- fondos -----
    public static final Color FONDO = new Color(15, 17, 41);
    public static final Color FONDO_DIBUJO = new Color(10, 12, 32);
    public static final Color TARJETA = new Color(27, 31, 66);
    public static final Color TARJETA_CLARA = new Color(44, 50, 102);
    public static final Color BORDE = new Color(76, 84, 160);
    public static final Color DESHABILITADO = new Color(52, 57, 98);

    // ----- textos -----
    public static final Color TEXTO = new Color(238, 241, 255);
    public static final Color TEXTO_SUAVE = new Color(150, 160, 215);

    // ----- colores vivos -----
    public static final Color ACTIVO = new Color(255, 214, 10);     // amarillo: lo que está pasando ahora
    public static final Color VERDE = new Color(46, 213, 115);
    public static final Color ROJO = new Color(255, 85, 95);
    public static final Color ROSA = new Color(255, 84, 150);
    public static final Color AZUL = new Color(72, 149, 255);
    public static final Color NARANJA = new Color(255, 159, 67);
    public static final Color TURQUESA = new Color(0, 214, 190);
    public static final Color MORADO = new Color(170, 120, 255);
    public static final Color GRIS = new Color(112, 120, 170);

    // ----- un color para cada nivel -----
    public static final Color NIVEL3 = new Color(0, 214, 143);
    public static final Color NIVEL2 = AZUL;
    public static final Color NIVEL1 = NARANJA;
    public static final Color NIVEL0 = new Color(224, 86, 253);

    private static final Color[] PALETA = {AZUL, VERDE, NARANJA, MORADO, TURQUESA, ROSA};

    // ----- fuentes -----
    public static final Font NORMAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font NEGRITA = new Font("SansSerif", Font.BOLD, 14);
    public static final Font GRANDE = new Font("SansSerif", Font.BOLD, 18);
    public static final Font TITULO = new Font("SansSerif", Font.BOLD, 26);
    public static final Font SIMBOLO = new Font("Monospaced", Font.BOLD, 20);

    /** Colores de los menús desplegables (se llama una vez al iniciar el programa). */
    public static void aplicarTema() {
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(ACTIVO));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(FONDO));
        UIManager.put("ComboBox.background", new ColorUIResource(TARJETA_CLARA));
        UIManager.put("ComboBox.foreground", new ColorUIResource(TEXTO));
        UIManager.put("ComboBox.buttonBackground", new ColorUIResource(TARJETA_CLARA));
        UIManager.put("ComboBox.buttonShadow", new ColorUIResource(BORDE));
        UIManager.put("ComboBox.buttonDarkShadow", new ColorUIResource(BORDE));
        UIManager.put("ComboBox.buttonHighlight", new ColorUIResource(TARJETA_CLARA));
        UIManager.put("Spinner.background", new ColorUIResource(TARJETA_CLARA));
        UIManager.put("Spinner.foreground", new ColorUIResource(TEXTO));
    }

    // ===================== herramientas de dibujo =====================

    public static void activarSuavizado(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    /** Mezcla dos colores: t = 0 da el color a, t = 1 da el color b. */
    public static Color mezclar(Color a, Color b, double t) {
        int r = (int) Math.round(a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = (int) Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int az = (int) Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, az);
    }

    public static Color conAlfa(Color c, int alfa) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alfa);
    }

    /** Cada símbolo tiene su propio color, así se reconoce fácil en la cadena, la pila y la cinta. */
    public static Color colorSimbolo(char c) {
        switch (c) {
            case 'a': case 'A': case '(':
                return AZUL;
            case 'b': case 'B':
                return VERDE;
            case 'c': case 'C':
                return NARANJA;
            case 'X':
                return MORADO;
            case 'Y':
                return TURQUESA;
            case 'Z': case 'P': case ')': case '1':
                return ROSA;
            case '0':
                return TURQUESA;
            case '+':
                return ACTIVO;
            case '_':
                return TARJETA_CLARA;
            case '[': case ']':
                return GRIS;
            default:
                return PALETA[Math.abs(c) % PALETA.length];
        }
    }

    /** Cuadrícula tenue de fondo, como papel milimetrado. */
    public static void pintarCuadricula(Graphics2D g2, int ancho, int alto) {
        g2.setColor(new Color(255, 255, 255, 9));
        for (int x = 0; x < ancho; x += 32) {
            g2.drawLine(x, 0, x, alto);
        }
        for (int y = 0; y < alto; y += 32) {
            g2.drawLine(0, y, ancho, y);
        }
    }

    /** Resplandor de color alrededor de un punto (se usa para resaltar lo que está activo). */
    public static void pintarBrillo(Graphics2D g2, double x, double y, double radio, Color color) {
        for (int i = 4; i >= 1; i--) {
            double r = radio + i * 6;
            g2.setColor(conAlfa(color, 30));
            g2.fill(new Ellipse2D.Double(x - r, y - r, 2 * r, 2 * r));
        }
    }
}
