package Truco.vista;

import Truco.controlador.ControladorJuego;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private PanelCartasJugador panelCartasJugador;
    private PanelAcciones panelAcciones;
    private PanelMensajes panelMensajes;
    private PanelPuntuacion panelPuntuacion;

    public VentanaPrincipal() {
        setTitle("Truco Argentino - 1 vs 1");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializar paneles
        panelCartasJugador = new PanelCartasJugador();
        panelAcciones = new PanelAcciones();
        panelMensajes = new PanelMensajes();
        panelPuntuacion = new PanelPuntuacion();


        // Agregar a la ventana
        add(panelCartasJugador, BorderLayout.SOUTH);
        add(panelAcciones, BorderLayout.EAST);
        add(panelMensajes, BorderLayout.CENTER);
        add(panelPuntuacion, BorderLayout.NORTH);

        // Crear el controlador y pasarle los paneles
        new ControladorJuego(panelCartasJugador, panelAcciones, panelMensajes, panelPuntuacion);

        setVisible(true);
    }
}
