package Truco.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class VentanaInicio extends JFrame {

    public VentanaInicio() {
        setTitle("Truco Argentino");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Truco Argentino", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        add(titulo, BorderLayout.CENTER);

        JButton btnIniciar = new JButton("Iniciar Juego");
        btnIniciar.setFont(new Font("Arial", Font.PLAIN, 18));
        btnIniciar.addActionListener(this::iniciarJuego);
        add(btnIniciar, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Centrar
        setVisible(true);
    }

    private void iniciarJuego(ActionEvent e) {
        new VentanaPrincipal(); // Lanza la ventana principal del juego
        dispose(); // Cierra la ventana de inicio
    }

    // Main para testeo rápido
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaInicio::new);
    }
}
