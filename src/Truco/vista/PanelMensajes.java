package Truco.vista;

import javax.swing.*;
import java.awt.*;

public class PanelMensajes extends JPanel {

    private JTextArea areaMensajes;
    private JScrollPane scrollPane;

    public PanelMensajes() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Mensajes del juego"));

        areaMensajes = new JTextArea();
        areaMensajes.setEditable(false);
        areaMensajes.setLineWrap(true);
        areaMensajes.setWrapStyleWord(true);

        scrollPane = new JScrollPane(areaMensajes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }

    // Metodo para agregar un nuevo mensaje
    public void agregarMensaje(String mensaje) {
        areaMensajes.append(mensaje + "\n");
        areaMensajes.setCaretPosition(areaMensajes.getDocument().getLength()); // Auto-scroll
    }

    // Metodo para limpiar la consola
    public void limpiarMensajes() {
        areaMensajes.setText("");
    }
}
