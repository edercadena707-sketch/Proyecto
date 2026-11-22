package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import MASAMORRA.logica.Maquina;
import MASAMORRA.logica.Paso;

/**
 * Clase padre de los tres dibujos (autómata, pila y cinta).
 * Pinta el fondo (degradado con cuadrícula) y guarda la máquina y el paso que se está mostrando;
 * cada hija sabe cómo dibujar su parte.
 */
public abstract class PanelDibujo extends JPanel {
    protected Maquina maquina;               // la máquina seleccionada
    protected Paso paso;                     // el paso actual (null si todavía no se ha simulado)
    protected Color acento = Estilo.AZUL;    // el color del nivel

    public PanelDibujo() {
        setBackground(Estilo.FONDO_DIBUJO);
    }

    public void setAcento(Color acento) {
        this.acento = acento;
    }

    public void setMaquina(Maquina maquina) {
        this.maquina = maquina;
        this.paso = null;
        repaint();
    }

    public void setPaso(Paso paso) {
        this.paso = paso;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        Estilo.activarSuavizado(g2);
        g2.setPaint(new GradientPaint(0, 0, Estilo.FONDO_DIBUJO,
                0, getHeight(), Estilo.mezclar(Estilo.FONDO_DIBUJO, acento, 0.16)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        Estilo.pintarCuadricula(g2, getWidth(), getHeight());
    }
}
