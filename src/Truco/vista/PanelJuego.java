package Truco.vista;

import javax.swing.*;
import java.awt.*;

public class PanelJuego extends JPanel {
    public PanelJuego() {
        setLayout(new BorderLayout());

        add(new PanelCartasJugador(), BorderLayout.SOUTH);
        add(new PanelAcciones(), BorderLayout.EAST);
        add(new PanelMensajes(), BorderLayout.CENTER);
    }
}
