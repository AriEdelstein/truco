package Truco.vista;

import javax.swing.*;
import java.awt.*;

public class PanelPuntuacion extends JPanel {

    private final JLabel lblPuntosHumano;
    private final JLabel lblPuntosCPU;

    public PanelPuntuacion() {
        setLayout(new GridLayout(2, 1, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Puntuación"));

        lblPuntosHumano = new JLabel("Vos: 0 puntos");
        lblPuntosCPU = new JLabel("CPU: 0 puntos");

        lblPuntosHumano.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblPuntosCPU.setFont(new Font("SansSerif", Font.BOLD, 16));

        add(lblPuntosHumano);
        add(lblPuntosCPU);
    }

    public void actualizarPuntos(int puntosHumano, int puntosCPU) {
        lblPuntosHumano.setText("Vos: " + puntosHumano + " puntos");
        lblPuntosCPU.setText("CPU: " + puntosCPU + " puntos");
    }
}
