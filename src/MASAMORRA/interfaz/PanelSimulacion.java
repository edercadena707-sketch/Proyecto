package MASAMORRA.interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import MASAMORRA.logica.Maquina;
import MASAMORRA.logica.Paso;
import MASAMORRA.logica.Resultado;

/**
 * Pantalla de un nivel. Es la misma para los cuatro niveles; lo único que cambia es
 * la lista de máquinas, el dibujo que se le pasa (autómata, pila o cinta) y el color del nivel.
 *
 * Funcionamiento: al presionar "Simular" se obtiene el Resultado (lista de pasos) y
 * luego los botones solo se mueven por esa lista (indice) y le piden al dibujo que pinte ese paso.
 */
public class PanelSimulacion extends JPanel implements ActionListener {
    private PanelDibujo dibujo;
    private PanelCadena panelCadena = new PanelCadena();
    private boolean mostrarCadena;
    private Color acento;

    private JComboBox<Maquina> cmbMaquinas = new JComboBox<>();
    private JComboBox<String> cmbEjemplos = new JComboBox<>();
    private ArrayList<String> cadenasEjemplo = new ArrayList<>();   // la cadena real de cada ejemplo del combo
    private JTextField txtCadena = new JTextField(11);
    private JPanel panelExtra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    private JLabel lblDescripcion = new JLabel(" ");

    private BotonColor btnSimular;
    private BotonColor btnReiniciar;
    private BotonColor btnAnterior;
    private BotonColor btnSiguiente;
    private BotonColor btnReproducir;
    private JLabel lblPaso = new JLabel("Sin simulación");
    private PanelRedondeado bannerResultado = new PanelRedondeado(Estilo.TARJETA, Estilo.BORDE);
    private JLabel lblResultado = new JLabel(" ", JLabel.CENTER);

    private DefaultListModel<String> modeloPasos = new DefaultListModel<>();
    private JList<String> listaPasos = new JList<>(modeloPasos);
    private RenderPasos renderPasos;

    private Maquina maquinaActual;
    private Resultado resultado;     // null mientras no se haya simulado nada
    private String cadenaActual = "";
    private int indice = 0;          // paso que se está mostrando
    private Timer timer;             // avanza solo cuando se presiona Reproducir
    private boolean cargando = false;   // true mientras se llenan los combos (para ignorar sus eventos)

    public PanelSimulacion(String nivel, String titulo, ArrayList<Maquina> maquinas,
                           PanelDibujo dibujo, boolean mostrarCadena, Color acento) {
        this.dibujo = dibujo;
        this.mostrarCadena = mostrarCadena;
        this.acento = acento;
        this.timer = new Timer(800, this);   // un paso cada 0,8 segundos
        this.renderPasos = new RenderPasos(acento);

        dibujo.setAcento(acento);
        panelCadena.setAcento(acento);

        btnSimular = crearBoton("Simular", acento);
        btnReiniciar = crearBoton("Inicio", Estilo.MORADO);
        btnAnterior = crearBoton("Anterior", Estilo.AZUL);
        btnSiguiente = crearBoton("Siguiente", Estilo.TURQUESA);
        btnReproducir = crearBoton("Reproducir", Estilo.VERDE);

        construirInterfaz(nivel, titulo);

        cargando = true;
        for (Maquina m : maquinas) {
            cmbMaquinas.addItem(m);
        }
        cargando = false;
        cambiarMaquina();
    }

    // ===================== construcción de la pantalla =====================

    private BotonColor crearBoton(String texto, Color color) {
        BotonColor boton = new BotonColor(texto, color);
        boton.addActionListener(this);
        return boton;
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Estilo.NEGRITA);
        etiqueta.setForeground(Estilo.TEXTO_SUAVE);
        return etiqueta;
    }

    private JPanel crearFila() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        fila.setOpaque(false);
        return fila;
    }

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(Estilo.NORMAL);
        combo.setBackground(Estilo.TARJETA_CLARA);
        combo.setForeground(Estilo.TEXTO);
        combo.addActionListener(this);
    }

    private void construirInterfaz(String nivel, String titulo) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        setBackground(Estilo.FONDO);

        // ---------- cabecera de color ----------
        PanelDegradado cabecera = new PanelDegradado(acento.darker().darker(), acento, nivel);
        cabecera.setLayout(new GridLayout(2, 1));
        cabecera.setPreferredSize(new Dimension(100, 62));
        cabecera.setBorder(BorderFactory.createEmptyBorder(7, 84, 5, 20));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Estilo.TITULO.deriveFont(20f));
        lblTitulo.setForeground(Color.WHITE);
        lblDescripcion.setFont(Estilo.NORMAL.deriveFont(13f));
        lblDescripcion.setForeground(new Color(255, 255, 255, 225));
        cabecera.add(lblTitulo);
        cabecera.add(lblDescripcion);

        // ---------- tarjeta de controles: una fila (y una segunda si el nivel agrega controles extra) ----------
        PanelRedondeado controles = new PanelRedondeado(Estilo.TARJETA, Estilo.BORDE);
        controles.setLayout(new BorderLayout());
        controles.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));

        JPanel fila = crearFila();
        fila.add(crearEtiqueta("Máquina:"));
        estilizarCombo(cmbMaquinas);
        fila.add(cmbMaquinas);
        fila.add(crearEtiqueta("   Ejemplos:"));
        estilizarCombo(cmbEjemplos);
        fila.add(cmbEjemplos);
        fila.add(crearEtiqueta("   Cadena:"));
        txtCadena.setFont(Estilo.SIMBOLO);
        txtCadena.setBackground(Estilo.FONDO_DIBUJO);
        txtCadena.setForeground(Estilo.ACTIVO);
        txtCadena.setCaretColor(Estilo.ACTIVO);
        txtCadena.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(acento, 2), BorderFactory.createEmptyBorder(2, 8, 2, 8)));
        txtCadena.addActionListener(this);      // Enter también simula
        fila.add(txtCadena);
        fila.add(btnSimular);
        controles.add(fila, BorderLayout.NORTH);
        panelExtra.setOpaque(false);
        controles.add(panelExtra, BorderLayout.SOUTH);

        JPanel norte = new JPanel(new BorderLayout(0, 8));
        norte.setOpaque(false);
        norte.add(cabecera, BorderLayout.NORTH);
        norte.add(controles, BorderLayout.CENTER);
        add(norte, BorderLayout.NORTH);

        // ---------- centro: el dibujo ocupa todo el espacio que queda ----------
        dibujo.setBorder(BorderFactory.createLineBorder(Estilo.BORDE, 2));
        add(dibujo, BorderLayout.CENTER);

        // ---------- derecha: la cadena de entrada (niveles 3 y 2) y la lista de pasos ----------
        listaPasos.setBackground(Estilo.TARJETA);
        listaPasos.setFocusable(false);
        listaPasos.setCellRenderer(renderPasos);
        JScrollPane scroll = new JScrollPane(listaPasos);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Estilo.TARJETA);
        scroll.getVerticalScrollBar().setUI(new ScrollOscuro(acento));
        scroll.getHorizontalScrollBar().setUI(new ScrollOscuro(acento));

        JLabel tituloPasos = new JLabel("PASOS");
        tituloPasos.setFont(Estilo.NEGRITA);
        tituloPasos.setForeground(acento);
        tituloPasos.setBorder(BorderFactory.createEmptyBorder(10, 14, 8, 10));

        PanelRedondeado tarjetaPasos = new PanelRedondeado(Estilo.TARJETA, Estilo.BORDE);
        tarjetaPasos.setLayout(new BorderLayout());
        tarjetaPasos.setBorder(BorderFactory.createEmptyBorder(2, 4, 8, 4));
        tarjetaPasos.add(tituloPasos, BorderLayout.NORTH);
        tarjetaPasos.add(scroll, BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout(0, 10));
        derecha.setOpaque(false);
        derecha.setPreferredSize(new Dimension(370, 100));
        if (mostrarCadena) {
            derecha.add(panelCadena, BorderLayout.NORTH);
        }
        derecha.add(tarjetaPasos, BorderLayout.CENTER);
        add(derecha, BorderLayout.EAST);

        // ---------- abajo: botones de reproducción y, al lado, el resultado ----------
        JPanel filaControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 3));
        filaControles.setOpaque(false);
        lblPaso.setFont(Estilo.NEGRITA);
        lblPaso.setForeground(Estilo.ACTIVO);
        filaControles.add(btnReiniciar);
        filaControles.add(btnAnterior);
        filaControles.add(btnSiguiente);
        filaControles.add(btnReproducir);
        filaControles.add(lblPaso);

        lblResultado.setFont(Estilo.NEGRITA);
        lblResultado.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        bannerResultado.setLayout(new BorderLayout());
        bannerResultado.setPreferredSize(new Dimension(100, 46));
        bannerResultado.add(lblResultado, BorderLayout.CENTER);

        JPanel sur = new JPanel(new BorderLayout(12, 0));
        sur.setOpaque(false);
        sur.add(filaControles, BorderLayout.WEST);
        sur.add(bannerResultado, BorderLayout.CENTER);
        add(sur, BorderLayout.SOUTH);
    }

    // ===================== métodos públicos =====================

    /** Panel vacío al lado del selector de máquina, para que un nivel agregue sus propios controles. */
    public JPanel getPanelExtra() {
        return panelExtra;
    }

    /** Agrega una máquina nueva a la lista y la deja seleccionada. */
    public void agregarMaquina(Maquina maquina) {
        cargando = true;
        cmbMaquinas.addItem(maquina);
        cmbMaquinas.setSelectedItem(maquina);
        cargando = false;
        cambiarMaquina();
    }

    // ===================== lógica de la pantalla =====================

    // Se llama cuando el usuario escoge otra máquina
    private void cambiarMaquina() {
        maquinaActual = (Maquina) cmbMaquinas.getSelectedItem();
        if (maquinaActual == null) {
            return;
        }
        lblDescripcion.setText(maquinaActual.getDescripcion());
        dibujo.setMaquina(maquinaActual);

        // se llena el combo de ejemplos
        cargando = true;
        cmbEjemplos.removeAllItems();
        cadenasEjemplo.clear();
        for (String cadena : maquinaActual.getAceptadas()) {
            cadenasEjemplo.add(cadena);
            cmbEjemplos.addItem(textoVisible(cadena) + "    (debe ACEPTAR)");
        }
        for (String cadena : maquinaActual.getRechazadas()) {
            cadenasEjemplo.add(cadena);
            cmbEjemplos.addItem(textoVisible(cadena) + "    (debe RECHAZAR)");
        }
        cargando = false;

        if (cadenasEjemplo.size() > 0) {
            cmbEjemplos.setSelectedIndex(0);
            txtCadena.setText(cadenasEjemplo.get(0));
        } else {
            txtCadena.setText("");
        }
        limpiar();
    }

    private String textoVisible(String cadena) {
        if (cadena.equals("")) {
            return "(cadena vacía)";
        }
        return cadena;
    }

    // El banner de abajo cambia de color según lo que esté pasando
    private void mostrarMensaje(String texto, Color colorTexto, Color fondo, Color borde) {
        String seguro = texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        lblResultado.setText("<html><div style='text-align:center'>" + seguro + "</div></html>");
        lblResultado.setForeground(colorTexto);
        bannerResultado.setColores(fondo, borde);
    }

    // Quita todo lo de la simulación anterior
    private void limpiar() {
        detener();
        resultado = null;
        indice = 0;
        modeloPasos.clear();
        dibujo.setPaso(null);
        panelCadena.setCadena(txtCadena.getText().trim(), -1);
        lblPaso.setText("Sin simulación");
        mostrarMensaje("Elige un ejemplo o escribe una cadena y presiona Simular",
                Estilo.TEXTO_SUAVE, Estilo.TARJETA, Estilo.BORDE);
        actualizarBotones();
    }

    private void simular() {
        if (maquinaActual == null) {
            return;
        }
        detener();
        cadenaActual = txtCadena.getText().trim();
        resultado = maquinaActual.simular(cadenaActual);

        modeloPasos.clear();
        for (int i = 0; i < resultado.getPasos().size(); i++) {
            modeloPasos.addElement(i + ".  " + resultado.getPasos().get(i).getDescripcion());
        }
        indice = 0;
        mostrarPaso();
    }

    // Pinta el paso número "indice" de la simulación
    private void mostrarPaso() {
        if (resultado == null) {
            return;
        }
        int ultimo = resultado.getPasos().size() - 1;
        Paso paso = resultado.getPasos().get(indice);

        dibujo.setPaso(paso);
        panelCadena.setCadena(cadenaActual, paso.getPosicion());
        renderPasos.setActual(indice);
        listaPasos.ensureIndexIsVisible(indice);
        listaPasos.repaint();
        lblPaso.setText("Paso " + indice + " de " + ultimo);

        if (indice == ultimo) {
            if (resultado.isAceptada()) {
                mostrarMensaje("CADENA ACEPTADA: " + resultado.getMotivo(),
                        new Color(140, 255, 190), new Color(14, 72, 48), Estilo.VERDE);
            } else {
                mostrarMensaje("CADENA RECHAZADA: " + resultado.getMotivo(),
                        new Color(255, 160, 170), new Color(86, 22, 40), Estilo.ROJO);
            }
        } else {
            mostrarMensaje("Procesando la cadena...", Estilo.TEXTO, Estilo.TARJETA, acento);
        }
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean hay = (resultado != null);
        int ultimo = 0;
        if (hay) {
            ultimo = resultado.getPasos().size() - 1;
        }
        btnReiniciar.setEnabled(hay);
        btnAnterior.setEnabled(hay && indice > 0);
        btnSiguiente.setEnabled(hay && indice < ultimo);
    }

    private void siguiente() {
        if (resultado != null && indice < resultado.getPasos().size() - 1) {
            indice++;
            mostrarPaso();
        }
    }

    private void anterior() {
        if (resultado != null && indice > 0) {
            indice--;
            mostrarPaso();
        }
    }

    private void reiniciar() {
        detener();
        indice = 0;
        mostrarPaso();
    }

    private void alternarReproduccion() {
        if (timer.isRunning()) {
            detener();
            return;
        }
        if (resultado == null) {
            simular();
        }
        if (indice >= resultado.getPasos().size() - 1) {
            indice = 0;          // si ya estaba al final, vuelve a empezar
            mostrarPaso();
        }
        timer.start();
        btnReproducir.setText("Pausar");
    }

    private void detener() {
        timer.stop();
        btnReproducir.setText("Reproducir");
    }

    // ===================== eventos =====================

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();

        if (origen == cmbMaquinas) {
            if (!cargando) {
                cambiarMaquina();
            }
        } else if (origen == cmbEjemplos) {
            if (!cargando && cmbEjemplos.getSelectedIndex() >= 0) {
                txtCadena.setText(cadenasEjemplo.get(cmbEjemplos.getSelectedIndex()));
                simular();
            }
        } else if (origen == btnSimular || origen == txtCadena) {
            simular();
        } else if (origen == btnSiguiente) {
            siguiente();
        } else if (origen == btnAnterior) {
            anterior();
        } else if (origen == btnReiniciar) {
            reiniciar();
        } else if (origen == btnReproducir) {
            alternarReproduccion();
        } else if (origen == timer) {
            // cada "tick" del temporizador avanza un paso; al llegar al final se detiene
            if (resultado != null && indice < resultado.getPasos().size() - 1) {
                siguiente();
            }
            if (resultado == null || indice >= resultado.getPasos().size() - 1) {
                detener();
            }
        }
    }
}
