package Truco.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelAcciones extends JPanel {

    private JButton btnTruco;
    private JButton btnEnvido;
    private JButton btnMazo;

    public PanelAcciones() {
        setLayout(new GridLayout(3, 1, 10, 10)); // 3 botones verticales
        setBorder(BorderFactory.createTitledBorder("Acciones"));

        btnTruco = new JButton("Cantar Truco");
        btnEnvido = new JButton("Cantar Envido");
        btnMazo = new JButton("Irse al Mazo");

        add(btnTruco);
        add(btnEnvido);
        add(btnMazo);
    }

    // Métodos para registrar listeners desde el controlador
    public void setAccionTruco(ActionListener listener) {
        btnTruco.addActionListener(listener);
    }

    public void setAccionEnvido(ActionListener listener) {
        btnEnvido.addActionListener(listener);
    }

    public void setAccionMazo(ActionListener listener) {
        btnMazo.addActionListener(listener);
    }

    // Habilitar/deshabilitar todos los botones
    public void habilitarAcciones(boolean habilitar) {
        btnTruco.setEnabled(habilitar);
        btnEnvido.setEnabled(habilitar);
        btnMazo.setEnabled(habilitar);
    }

    // Habilitar/deshabilitar solo el botón de Envido
    public void habilitarBotonEnvido(boolean habilitar) {
        btnEnvido.setEnabled(habilitar);
    }

    // Habilitar/deshabilitar solo el botón de Truco
    public void habilitarBotonTruco(boolean habilitar) {
        btnTruco.setEnabled(habilitar);
    }
}
