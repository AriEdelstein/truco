package Truco.juego;

import Truco.jugadores.Jugador;

public class Puntuacion {
    // Referencias a los dos jugadores del juego
    private final Jugador jugador1;
    private final Jugador jugador2;

    // Constructor: recibe los dos jugadores a seguir
    public Puntuacion(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
    }

    // Suma puntos al jugador pasado como parámetro y muestra el resultado por consola
    public void sumarPuntos(Jugador jugador, int puntos) {
        jugador.sumarPuntos(puntos); // Llama al método de la clase Jugador
        System.out.println(jugador.getNombre() + " gana " + puntos + " puntos. Total: " + jugador.getPuntos());
    }

    // Retorna true si alguno de los jugadores llegó a 30 puntos o más
    public boolean hayGanador() {
        return jugador1.getPuntos() >= 30 || jugador2.getPuntos() >= 30;
    }

    // Devuelve el jugador que haya alcanzado o superado los 30 puntos, o null si no hay ganador aún
    public Jugador obtenerGanador() {
        if (jugador1.getPuntos() >= 30) return jugador1;
        if (jugador2.getPuntos() >= 30) return jugador2;
        return null;
    }

    // Muestra los puntajes actuales de ambos jugadores
    public void mostrarPuntajes() {
        System.out.println("🧮 Puntajes:");
        System.out.println("- " + jugador1.getNombre() + ": " + jugador1.getPuntos() + " puntos");
        System.out.println("- " + jugador2.getNombre() + ": " + jugador2.getPuntos() + " puntos\n");
    }
}
