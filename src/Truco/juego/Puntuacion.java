package Truco.juego;

import Truco.jugadores.Jugador;

public class Puntuacion {
    private final Jugador jugador1;
    private final Jugador jugador2;

    public Puntuacion(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    public void sumarPuntos(Jugador jugador, int puntos) {
        jugador.sumarPuntos(puntos);
        System.out.println(jugador.getNombre() + " gana " + puntos + " puntos. Total: " + jugador.getPuntos());
    }

    public boolean hayGanador() {
        return jugador1.getPuntos() >= 30 || jugador2.getPuntos() >= 30;
    }

    public Jugador obtenerGanador() {
        if (jugador1.getPuntos() >= 30) return jugador1;
        if (jugador2.getPuntos() >= 30) return jugador2;
        return null;
    }

    public void mostrarPuntajes() {
        System.out.println("🧮 Puntajes:");
        System.out.println("- " + jugador1.getNombre() + ": " + jugador1.getPuntos() + " puntos");
        System.out.println("- " + jugador2.getNombre() + ": " + jugador2.getPuntos() + " puntos\n");
    }
}
