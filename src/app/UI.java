package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import app.cifrados.*;

public class UI extends JFrame {

    private static final Color FONDO = new Color(23, 78, 166);
    private static final Color PANEL = new Color(45, 102, 196);
    private static final Color ACENTO = new Color(224, 239, 255);
    private static final Color AZUL_OSCURO = new Color(10, 35, 78);
    private static final Color ENTRADA = new Color(209, 228, 255);
    private static final Color SALIDA = new Color(188, 216, 255);
    private static final Color TABLERO = new Color(168, 202, 247);
    private static final Color TEXTO = new Color(12, 37, 74);

    private JComboBox<String> metodoBox;
    private JTextArea textoOriginal;
    private JTextArea textoCifrado;
    private JTextArea descripcionArea;
    private JSplitPane splitPane;
    private JTextArea tableroPlayfair;
    private JTextField claveField;
    private JLabel claveLabel;
    private JLabel claveTipoLabel;
    private JLabel tableroLabel;
    private String ultimoCampoEditado = "original";

    public UI() {
        setTitle("Métodos de encriptado Clásico");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(FONDO);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(PANEL);
        topPanel.setBorder(new EmptyBorder(10, 12, 10, 12));
        metodoBox = new JComboBox<>(new String[]{
                "Cesar", "Atbash", "Vigenere", "Rail Fence", "Playfair"
        });

        topPanel.add(new JLabel("Metodo:"));
        topPanel.add(metodoBox);

        claveLabel = new JLabel("Clave:");
        claveField = new JTextField(10);
        claveTipoLabel = new JLabel();

        topPanel.add(claveLabel);
        topPanel.add(claveField);
        topPanel.add(claveTipoLabel);

        descripcionArea = new JTextArea(4, 20);
        descripcionArea.setEditable(false);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setOpaque(false);
        descripcionArea.setForeground(TEXTO);
        descripcionArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descripcionArea.setBorder(new EmptyBorder(4, 12, 10, 12));

        JPanel headerPanel = new JPanel(new BorderLayout(0, 8));
        headerPanel.setBackground(PANEL);
        headerPanel.setBorder(new EmptyBorder(8, 8, 4, 8));
        headerPanel.add(topPanel, BorderLayout.NORTH);
        headerPanel.add(descripcionArea, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        textoOriginal = new JTextArea();
        textoCifrado = new JTextArea();
        textoOriginal.setBackground(ENTRADA);
        textoCifrado.setBackground(SALIDA);
        textoOriginal.setForeground(TEXTO);
        textoCifrado.setForeground(TEXTO);
        textoOriginal.setBorder(new EmptyBorder(8, 8, 8, 8));
        textoCifrado.setBorder(new EmptyBorder(8, 8, 8, 8));

        JPanel panelOriginal = new JPanel(new BorderLayout());
        panelOriginal.setBackground(FONDO);
        panelOriginal.setBorder(new EmptyBorder(8, 8, 8, 4));
        panelOriginal.add(new JLabel("Texto original:"), BorderLayout.NORTH);
        panelOriginal.add(new JScrollPane(textoOriginal), BorderLayout.CENTER);

        JPanel panelCifrado = new JPanel(new BorderLayout());
        panelCifrado.setBackground(FONDO);
        panelCifrado.setBorder(new EmptyBorder(8, 4, 8, 8));
        panelCifrado.add(new JLabel("Texto cifrado:"), BorderLayout.NORTH);
        panelCifrado.add(new JScrollPane(textoCifrado), BorderLayout.CENTER);

        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                panelOriginal,
                panelCifrado
        );
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setBorder(null);
        splitPane.setBackground(FONDO);
        add(splitPane, BorderLayout.CENTER);

        tableroPlayfair = new JTextArea(5, 20);
        tableroPlayfair.setEditable(false);
        tableroPlayfair.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
        tableroPlayfair.setBackground(TABLERO);
        tableroPlayfair.setForeground(AZUL_OSCURO);
        tableroPlayfair.setBorder(new EmptyBorder(8, 8, 8, 8));

        tableroLabel = new JLabel("Tablero Playfair:");
        JScrollPane tableroScroll = new JScrollPane(tableroPlayfair);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(FONDO);
        bottomPanel.setBorder(new EmptyBorder(0, 8, 8, 8));
        bottomPanel.add(tableroLabel, BorderLayout.NORTH);
        bottomPanel.add(tableroScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        estilizarEtiqueta(claveLabel);
        estilizarEtiqueta(claveTipoLabel);
        estilizarEtiqueta(tableroLabel);
        for (Component componente : topPanel.getComponents()) {
            if (componente instanceof JLabel) {
                estilizarEtiqueta((JLabel) componente);
            } else if (componente instanceof JComboBox) {
                componente.setBackground(new Color(225, 237, 255));
                componente.setForeground(TEXTO);
            } else if (componente instanceof JTextField) {
                componente.setBackground(new Color(225, 237, 255));
                componente.setForeground(TEXTO);
            }
        }
        estilizarEtiqueta((JLabel) panelOriginal.getComponent(0));
        estilizarEtiqueta((JLabel) panelCifrado.getComponent(0));

        metodoBox.addActionListener(e -> actualizarUI());
        textoOriginal.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ultimoCampoEditado = "original";
                procesar();
            }
        });
        textoCifrado.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ultimoCampoEditado = "cifrado";
                procesar();
            }
        });
        claveField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                procesar();
            }
        });

        actualizarUI();
        setVisible(true);
        SwingUtilities.invokeLater(() -> splitPane.setDividerLocation(0.5));
    }

    private void actualizarUI() {
        String metodo = (String) metodoBox.getSelectedItem();

        boolean usaClave = metodo.equals("Cesar") ||
                metodo.equals("Vigenere") ||
                metodo.equals("Rail Fence") ||
                metodo.equals("Playfair");
        boolean usaTableroPlayfair = metodo.equals("Playfair");

        claveLabel.setVisible(usaClave);
        claveField.setVisible(usaClave);
        claveTipoLabel.setVisible(usaClave);
        claveTipoLabel.setText(obtenerTipoClave(metodo));
        tableroLabel.setVisible(usaTableroPlayfair);
        tableroPlayfair.setVisible(usaTableroPlayfair);
        descripcionArea.setText(obtenerDescripcion(metodo));
        claveField.setText("");
        textoOriginal.setText("");
        textoCifrado.setText("");
        tableroPlayfair.setText("");
        ultimoCampoEditado = "original";

        procesar();
    }

    private void procesar() {
        String metodo = (String) metodoBox.getSelectedItem();
        String clave = claveField.getText();

        tableroPlayfair.setText("");
        if (metodo.equals("Playfair")) {
            tableroPlayfair.setText(Playfair.obtenerTablero(clave));
        }

        if (ultimoCampoEditado.equals("original")) {
            textoCifrado.setText(transformar(metodo, textoOriginal.getText(), clave, true));
        } else {
            textoOriginal.setText(transformar(metodo, textoCifrado.getText(), clave, false));
        }
    }

    private String transformar(String metodo, String texto, String clave, boolean cifrar) {
        switch (metodo) {
            case "Cesar":
                return cifrar ? Cesar.cifrar(texto, clave) : Cesar.descifrar(texto, clave);
            case "Atbash":
                return cifrar ? Atbash.cifrar(texto) : Atbash.descifrar(texto);
            case "Vigenere":
                return cifrar ? Vigenere.cifrar(texto, clave) : Vigenere.descifrar(texto, clave);
            case "Rail Fence":
                return cifrar ? RailFence.cifrar(texto, clave) : RailFence.descifrar(texto, clave);
            case "Playfair":
                return cifrar ? Playfair.cifrar(texto, clave) : Playfair.descifrar(texto, clave);
            default:
                return "";
        }
    }

    private String obtenerDescripcion(String metodo) {
        String instruccionGeneral = "Escribe en 'Texto original' para cifrar o en 'Texto cifrado' para descifrar.";

        switch (metodo) {
            case "Cesar":
                return instruccionGeneral + "\nCesar desplaza cada letra un numero fijo de posiciones e incluye la letra \u00f1.";
            case "Atbash":
                return instruccionGeneral + "\nAtbash no necesita clave y sustituye cada letra por su opuesta en el alfabeto.";
            case "Vigenere":
                return instruccionGeneral + "\nVigenere usa una palabra clave repetida sobre el texto.";
            case "Rail Fence":
                return instruccionGeneral + "\nRail Fence usa un numero de rieles y elimina los espacios del mensaje.";
            case "Playfair":
                return instruccionGeneral + "\nPlayfair usa una palabra clave y al descifrar puede mantener letras de relleno como X o Q.";
            default:
                return "";
        }
    }

    private String obtenerTipoClave(String metodo) {
        switch (metodo) {
            case "Cesar":
                return "(numero)";
            case "Vigenere":
                return "(palabra)";
            case "Rail Fence":
                return "(numero de rieles)";
            case "Playfair":
                return "(palabra)";
            default:
                return "";
        }
    }

    private void estilizarEtiqueta(JLabel etiqueta) {
        etiqueta.setForeground(ACENTO);
        etiqueta.setFont(new Font("SansSerif", Font.BOLD, 13));
    }
}
