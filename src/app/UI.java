package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import app.cifrados.*;

public class UI extends JFrame {

    private JComboBox<String> metodoBox;
    private JTextArea textoOriginal;
    private JTextArea textoCifrado;
    private JTextField claveField;
    private JLabel claveLabel;

    public UI() {
        setTitle("Metodos de criptografia Clásica");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        metodoBox = new JComboBox<>(new String[]{
                "Cesar", "Atbash"
        });
        topPanel.add(new JLabel("Método:"));
        topPanel.add(metodoBox);

        claveLabel = new JLabel("Clave:");
        claveField = new JTextField(10);

        topPanel.add(claveLabel);
        topPanel.add(claveField);

        add(topPanel, BorderLayout.NORTH);

        textoOriginal = new JTextArea();
        textoCifrado = new JTextArea();
        textoCifrado.setEditable(false);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(textoOriginal),
                new JScrollPane(textoCifrado)
        );

        add(splitPane, BorderLayout.CENTER);

        metodoBox.addActionListener(e -> actualizarUI());
        textoOriginal.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                cifrar();
            }
        });
        claveField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                cifrar();
            }
        });

        actualizarUI();
        setVisible(true);
    }

    private void actualizarUI() {
        String metodo = (String) metodoBox.getSelectedItem();

        boolean usaClave = metodo.equals("Cesar")
                         ;

        claveLabel.setVisible(usaClave);
        claveField.setVisible(usaClave);

        cifrar();
    }

    private void cifrar() {
        String texto = textoOriginal.getText();
        String clave = claveField.getText();
        String metodo = (String) metodoBox.getSelectedItem();

        String resultado = "";

        switch (metodo) {
            case "Cesar":
                resultado = Cesar.cifrar(texto, clave);
                break;
            case "Atbash":
                resultado = Atbash.cifrar(texto);
                break;

        }

        textoCifrado.setText(resultado);
    }
}