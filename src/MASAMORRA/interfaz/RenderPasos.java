package MASAMORRA.interfaz;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Decide cómo se ve cada renglón de la lista de pasos: el paso actual en color,
 * los pasos ya recorridos en blanco y los que faltan atenuados.
 */
public class RenderPasos extends DefaultListCellRenderer {
    private Color acento;
    private int actual = -1;

    public RenderPasos(Color acento) {
        this.acento = acento;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                  boolean seleccionado, boolean foco) {
        JLabel etiqueta = (JLabel) super.getListCellRendererComponent(lista, valor, indice, false, false);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(7, 10, 7, 10));
        etiqueta.setFont(Estilo.NORMAL);
        if (indice == actual) {
            etiqueta.setBackground(acento);
            etiqueta.setForeground(Estilo.FONDO);
            etiqueta.setFont(Estilo.NEGRITA);
        } else {
            if (indice % 2 == 0) {
                etiqueta.setBackground(Estilo.TARJETA);
            } else {
                etiqueta.setBackground(Estilo.mezclar(Estilo.TARJETA, Estilo.FONDO, 0.35));
            }
            if (indice < actual) {
                etiqueta.setForeground(Estilo.TEXTO);
            } else {
                etiqueta.setForeground(Estilo.TEXTO_SUAVE);
            }
        }
        return etiqueta;
    }
}
