package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

/** Barra de desplazamiento delgada y de color, en lugar de la gris de Swing. */
public class ScrollOscuro extends BasicScrollBarUI {
    private Color acento;

    public ScrollOscuro(Color acento) {
        this.acento = acento;
    }

    private JButton botonVacio() {
        JButton boton = new JButton();
        boton.setPreferredSize(new Dimension(0, 0));
        boton.setMinimumSize(new Dimension(0, 0));
        boton.setMaximumSize(new Dimension(0, 0));
        return boton;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) { return botonVacio(); }

    @Override
    protected JButton createIncreaseButton(int orientation) { return botonVacio(); }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.setColor(Estilo.FONDO_DIBUJO);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        Graphics2D g2 = (Graphics2D) g.create();
        Estilo.activarSuavizado(g2);
        g2.setColor(acento);
        g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(12, 12);
    }
}
