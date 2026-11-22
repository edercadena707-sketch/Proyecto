package MASAMORRA.interfaz;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import MASAMORRA.logica.Ejemplos;

/**
 * Todo el contenido de la ventana. A la izquierda está el menú con los módulos y a la derecha
 * se muestra el módulo escogido (CardLayout: una "carta" por pantalla).
 */
public class PanelPrincipal extends JPanel implements ActionListener {
    private CardLayout cartas = new CardLayout();
    private JPanel contenedor = new JPanel(cartas);

    private String[] nombresCartas = {"inicio", "nivel3", "nivel2", "nivel1", "nivel0"};
    private BotonMenu[] botones = new BotonMenu[5];

    private PanelSimulacion panelNivel3;
    private PanelSimulacion panelNivel2;
    private PanelSimulacion panelNivel1;
    private PanelSimulacion panelNivel0;

    // controles del nivel 3 para crear un AFD parametrizable
    private JSpinner spnN = new JSpinner(new SpinnerNumberModel(5, 2, 7, 1));
    private BotonColor btnCrearAFD = new BotonColor("Crear AFD", Estilo.NIVEL3);

    public PanelPrincipal() {
        setLayout(new BorderLayout());
        setBackground(Estilo.FONDO);

        add(crearBarraSuperior(), BorderLayout.NORTH);

        // ---------- una pantalla por nivel ----------
        panelNivel3 = new PanelSimulacion("3", "Nivel 3 — Lenguajes regulares (Autómata Finito)",
                Ejemplos.nivel3(), new DibujoAFD(), true, Estilo.NIVEL3);
        panelNivel2 = new PanelSimulacion("2", "Nivel 2 — Libres de contexto (Autómata de Pila)",
                Ejemplos.nivel2(), new DibujoPila(), true, Estilo.NIVEL2);
        panelNivel1 = new PanelSimulacion("1", "Nivel 1 — Sensibles al contexto (cinta acotada)",
                Ejemplos.nivel1(), new DibujoCinta(), false, Estilo.NIVEL1);
        panelNivel0 = new PanelSimulacion("0", "Nivel 0 — Máquina de Turing (cinta extensible)",
                Ejemplos.nivel0(), new DibujoCinta(), false, Estilo.NIVEL0);

        // parametrizable: el usuario escoge n y se crea un AFD de binarios múltiplos de n
        JLabel lblParametro = new JLabel("   AFD parametrizable, múltiplos de n =");
        lblParametro.setFont(Estilo.NEGRITA);
        lblParametro.setForeground(Estilo.NIVEL3);
        spnN.setFont(Estilo.NEGRITA);
        spnN.setPreferredSize(new Dimension(64, 32));
        spnN.setBorder(BorderFactory.createLineBorder(Estilo.NIVEL3, 2));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spnN.getEditor();
        editor.getTextField().setBackground(Estilo.FONDO_DIBUJO);
        editor.getTextField().setForeground(Estilo.ACTIVO);
        editor.getTextField().setCaretColor(Estilo.ACTIVO);
        btnCrearAFD.addActionListener(this);
        panelNivel3.getPanelExtra().add(lblParametro);
        panelNivel3.getPanelExtra().add(spnN);
        panelNivel3.getPanelExtra().add(btnCrearAFD);

        contenedor.setBackground(Estilo.FONDO);
        contenedor.add(crearInicio(), nombresCartas[0]);
        contenedor.add(panelNivel3, nombresCartas[1]);
        contenedor.add(panelNivel2, nombresCartas[2]);
        contenedor.add(panelNivel1, nombresCartas[3]);
        contenedor.add(panelNivel0, nombresCartas[4]);
        add(contenedor, BorderLayout.CENTER);

        mostrar(0);
    }

    // Barra de arriba con los cinco botones, centrados
    private JPanel crearBarraSuperior() {
        PanelDegradado barra = new PanelDegradado(new Color(52, 28, 120), new Color(16, 18, 60), null);
        barra.setAdornos(false);
        barra.setRedondeo(0);
        barra.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 7));
        barra.setPreferredSize(new Dimension(100, 64));

        botones[0] = new BotonMenu("i", "Inicio", "Bienvenida", Estilo.ACTIVO);
        botones[1] = new BotonMenu("3", "Nivel 3", "Regulares · AFD", Estilo.NIVEL3);
        botones[2] = new BotonMenu("2", "Nivel 2", "Libres de contexto", Estilo.NIVEL2);
        botones[3] = new BotonMenu("1", "Nivel 1", "Sensibles · LBA", Estilo.NIVEL1);
        botones[4] = new BotonMenu("0", "Nivel 0", "Máquina de Turing", Estilo.NIVEL0);
        for (int i = 0; i < botones.length; i++) {
            botones[i].addActionListener(this);
            barra.add(botones[i]);
        }

        JPanel contenedorBarra = new JPanel(new BorderLayout());
        contenedorBarra.setOpaque(false);
        contenedorBarra.add(barra, BorderLayout.CENTER);
        contenedorBarra.add(new FranjaColores(), BorderLayout.SOUTH);   // franja con los cuatro colores
        return contenedorBarra;
    }

    // ===================== pantalla de bienvenida =====================

    private JPanel crearInicio() {
        JPanel panel = new JPanel(new BorderLayout(0, 18));
        panel.setBackground(Estilo.FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        // banner de color
        PanelDegradado banner = new PanelDegradado(new Color(110, 50, 230), new Color(255, 84, 150), null);
        banner.setLayout(new GridLayout(2, 1));
        banner.setPreferredSize(new Dimension(100, 120));
        banner.setBorder(BorderFactory.createEmptyBorder(24, 30, 16, 20));
        JLabel titulo = new JLabel("Simulador de la Jerarquía de Chomsky");
        titulo.setFont(Estilo.TITULO.deriveFont(32f));
        titulo.setForeground(Color.WHITE);
        JLabel subtitulo = new JLabel("Cuatro niveles, cuatro máquinas. Mira cómo procesan cada cadena, paso a paso.");
        subtitulo.setFont(Estilo.GRANDE);
        subtitulo.setForeground(new Color(255, 255, 255, 230));
        banner.add(titulo);
        banner.add(subtitulo);
        panel.add(banner, BorderLayout.NORTH);

        // una tarjeta de color por nivel
        JPanel tarjetas = new JPanel(new GridLayout(2, 2, 16, 16));
        tarjetas.setOpaque(false);
        tarjetas.add(crearTarjetaNivel("3", "Lenguajes regulares", "Autómata finito (AFD)",
                "Memoria: ninguna, solo el estado actual", Estilo.NIVEL3));
        tarjetas.add(crearTarjetaNivel("2", "Lenguajes libres de contexto", "Autómata de pila",
                "Memoria: una pila (LIFO)", Estilo.NIVEL2));
        tarjetas.add(crearTarjetaNivel("1", "Lenguajes sensibles al contexto", "Máquina de Turing acotada (LBA)",
                "Memoria: cinta del tamaño de la entrada", Estilo.NIVEL1));
        tarjetas.add(crearTarjetaNivel("0", "Lenguajes recursivamente enumerables", "Máquina de Turing",
                "Memoria: cinta infinita", Estilo.NIVEL0));
        panel.add(tarjetas, BorderLayout.CENTER);

        // cómo se usa
        PanelRedondeado ayuda = new PanelRedondeado(Estilo.TARJETA, Estilo.ACTIVO);
        ayuda.setLayout(new BorderLayout());
        ayuda.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel texto = new JLabel("<html><div style='width:880px'><span style='color:#FFD60A; font-size:15px'><b>¿Cómo se usa?</b></span><br>"
                + "<span style='color:#EEF1FF'>1) Escoge un nivel en el menú de arriba.&nbsp;&nbsp; 2) Elige una máquina y un ejemplo, o escribe tu propia cadena.&nbsp;&nbsp; "
                + "3) Presiona <b>Simular</b> y recorre los pasos con <b>Siguiente</b>, <b>Anterior</b> o <b>Reproducir</b>. "
                + "El paso actual se resalta en la lista de la derecha.</span></div></html>");
        texto.setFont(Estilo.NORMAL);
        ayuda.add(texto, BorderLayout.CENTER);
        panel.add(ayuda, BorderLayout.SOUTH);
        return panel;
    }

    private PanelRedondeado crearTarjetaNivel(String numero, String lenguajes, String maquina,
                                              String memoria, Color color) {
        PanelRedondeado tarjeta = new PanelRedondeado(Estilo.TARJETA, color);
        tarjeta.setLayout(new BorderLayout(18, 0));
        tarjeta.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 18));

        JLabel lblNumero = new JLabel(numero);
        lblNumero.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 84));
        lblNumero.setForeground(color);
        tarjeta.add(lblNumero, BorderLayout.WEST);

        String hex = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        JLabel lblTexto = new JLabel("<html><b style='font-size:15px; color:#FFFFFF'>" + lenguajes + "</b><br><br>"
                + "<span style='font-size:14px; color:" + hex + "'><b>" + maquina + "</b></span><br><br>"
                + "<span style='font-size:13px; color:#A0A8DC'>" + memoria + "</span></html>");
        tarjeta.add(lblTexto, BorderLayout.CENTER);
        return tarjeta;
    }

    // ===================== navegación =====================

    // Muestra la pantalla número "indice" y resalta su botón en el menú
    private void mostrar(int indice) {
        cartas.show(contenedor, nombresCartas[indice]);
        for (int i = 0; i < botones.length; i++) {
            botones[i].setSeleccionado(i == indice);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCrearAFD) {
            int n = (Integer) spnN.getValue();
            panelNivel3.agregarMaquina(Ejemplos.afdMultiplosDe(n));
            return;
        }
        for (int i = 0; i < botones.length; i++) {
            if (e.getSource() == botones[i]) {
                mostrar(i);
            }
        }
    }
}
