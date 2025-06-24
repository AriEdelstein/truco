package Truco.juego;

import Truco.cartas.Carta;
import Truco.jugadores.Jugador;
import Truco.util.Utils;

import java.util.*;

public class Ronda {
    private final Jugador jugadorMano; // Jugador que comienza la ronda
    private final Jugador jugadorPie;  // Jugador que va segundo

    private final List<Carta> jugadasMano = new ArrayList<>(); // Cartas jugadas por el jugador mano
    private final List<Carta> jugadasPie = new ArrayList<>();  // Cartas jugadas por el jugador pie
    private final List<ResultadoParcial> resultadosManos = new ArrayList<>(); // Resultado de cada mano (ronda parcial)
    private final Map<Jugador, List<Carta>> jugadas = new HashMap<>(); // Mapa jugador → cartas jugadas

    private Jugador ultimoJugadorQueJugo; // Referencia al último jugador que jugó
    private Jugador jugadorActual;        // Jugador que tiene el turno actual
    private Jugador otroJugador;          // El otro jugador (no actual)

    private final List<Jugador> ordenJugadoresPrimeros = new ArrayList<>(); // Para definir quién fue primero en cada mano
    private boolean rondaFinalizada; // Si la ronda ya terminó

    // Constructor: inicializa la ronda con los dos jugadores
    public Ronda(Jugador jugadorMano, Jugador jugadorPie) {
        this.jugadorMano = jugadorMano;
        this.jugadorPie = jugadorPie;
        this.jugadorActual = jugadorMano;
        this.otroJugador = jugadorPie;
    }

    // Método principal que permite jugar un turno
    public void jugarTurno() {
        if (rondaFinalizada) return;

        // Previene que un jugador juegue más cartas que el otro
        if (jugadorActual == jugadorMano && jugadasMano.size() > jugadasPie.size()) return;
        if (jugadorActual == jugadorPie && jugadasPie.size() > jugadasMano.size()) return;

        Carta jugada = jugadorActual.jugarCarta(); // Jugador juega una carta
        if (jugada == null) {
            System.err.println("⚠️ " + jugadorActual.getNombre() + " no tiene más cartas para jugar.");
            return;
        }

        System.out.println("---------------------------------------------");
        System.out.println(jugadorActual.getNombre() + " jugó: " + jugada);
        System.out.println("---------------------------------------------");

        // Guarda la jugada en la lista correspondiente
        if (jugadorActual == jugadorMano) {
            jugadasMano.add(jugada);
        } else {
            jugadasPie.add(jugada);
        }

        registrarJugada(jugadorActual, jugada); // Registra en el mapa general

        // Guarda quién jugó primero en esta mano
        if (jugadasMano.size() > jugadasPie.size()) {
            ordenJugadoresPrimeros.add(jugadorMano);
        } else if (jugadasPie.size() > jugadasMano.size()) {
            ordenJugadoresPrimeros.add(jugadorPie);
        }

        // Si ambos jugadores ya jugaron en esta mano:
        if (jugadasMano.size() == jugadasPie.size()) {
            evaluarMano(jugadasMano.size() - 1); // Evalúa el ganador de la mano actual

            if (verificarCierreAnticipado()) {
                rondaFinalizada = true;
                return;
            }

            if (jugadasMano.size() == 3 && jugadasPie.size() == 3) {
                rondaFinalizada = true;
            }

            actualizarJugadorActual(); // Define quién inicia la próxima mano
        } else {
            cambiarTurno(); // Cambia el turno si solo uno jugó
        }
    }

    // Compara las cartas jugadas en una mano y guarda el resultado parcial
    private void evaluarMano(int index) {
        Carta cartaMano = jugadasMano.get(index);
        Carta cartaPie = jugadasPie.get(index);

        int cmp = Utils.compararCartasTruco(cartaMano, cartaPie); // Usa la lógica de jerarquía del Truco
        if (cmp < 0) {
            resultadosManos.add(ResultadoParcial.GANA_PIE);
        } else if (cmp > 0) {
            resultadosManos.add(ResultadoParcial.GANA_MANO);
        } else {
            resultadosManos.add(ResultadoParcial.PARDA);
        }
    }

    // Decide quién debe empezar la siguiente mano
    private void actualizarJugadorActual() {
        ResultadoParcial ultimo = resultadosManos.get(resultadosManos.size() - 1);

        switch (ultimo) {
            case GANA_MANO -> setJugadorActual(jugadorMano);
            case GANA_PIE -> setJugadorActual(jugadorPie);
            case PARDA -> {
                Jugador quienFuePrimero = ordenJugadoresPrimeros.get(resultadosManos.size() - 1);
                setJugadorActual(quienFuePrimero);
            }
        }
    }

    // Alterna el turno entre jugadores
    private void cambiarTurno() {
        setJugadorActual(otroJugador);
    }

    // Actualiza el jugador actual y el otro
    private void setJugadorActual(Jugador nuevoActual) {
        jugadorActual = nuevoActual;
        otroJugador = (nuevoActual == jugadorMano) ? jugadorPie : jugadorMano;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    // Indica si la ronda se completó (ya se jugaron todas las manos o fue anticipadamente cerrada)
    public boolean rondaCompleta() {
        return rondaFinalizada || (jugadasMano.size() == 3 && jugadasPie.size() == 3);
    }

    // Determina el ganador de la ronda en base a las manos ganadas
    public Jugador determinarGanador() {
        int ganadasMano = 0;
        int ganadasPie = 0;

        for (ResultadoParcial r : resultadosManos) {
            switch (r) {
                case GANA_MANO -> ganadasMano++;
                case GANA_PIE -> ganadasPie++;
            }

            if (ganadasMano == 2) return jugadorMano;
            if (ganadasPie == 2) return jugadorPie;
        }

        // Si se jugó la tercera mano y fue parda, gana quien ganó la primera
        if (resultadosManos.size() == 3 && resultadosManos.get(2) == ResultadoParcial.PARDA) {
            return switch (resultadosManos.get(0)) {
                case GANA_MANO -> jugadorMano;
                case GANA_PIE -> jugadorPie;
                default -> jugadorMano; // Si primera también fue parda, gana el mano
            };
        }

        // En caso de empate parcial (parda + 1 ganada), gana el mano
        if (resultadosManos.size() == 2) {
            ResultadoParcial r1 = resultadosManos.get(0);
            ResultadoParcial r2 = resultadosManos.get(1);

            if ((r1 == ResultadoParcial.GANA_MANO && r2 == ResultadoParcial.PARDA) ||
                    (r2 == ResultadoParcial.GANA_MANO && r1 == ResultadoParcial.PARDA)) {
                return jugadorMano;
            }

            if ((r1 == ResultadoParcial.GANA_PIE && r2 == ResultadoParcial.PARDA) ||
                    (r2 == ResultadoParcial.GANA_PIE && r1 == ResultadoParcial.PARDA)) {
                return jugadorPie;
            }
        }

        // Si no se puede determinar por otro criterio, gana el jugador mano
        return jugadorMano;
    }

    // Reglas para cierre anticipado de la ronda
    private boolean verificarCierreAnticipado() {
        if (resultadosManos.size() < 2) return false;

        ResultadoParcial r1 = resultadosManos.get(0);
        ResultadoParcial r2 = resultadosManos.get(1);

        // Si la primera fue parda y hay un ganador en la segunda
        if (r1 == ResultadoParcial.PARDA) {
            return r2 == ResultadoParcial.GANA_MANO || r2 == ResultadoParcial.GANA_PIE;
        }

        // Si un jugador ganó ambas, ya no es necesario jugar la tercera
        return r1 == r2 && r1 != ResultadoParcial.PARDA;
    }

    // Muestra las cartas jugadas por cada jugador
    public void mostrarJugadas() {
        System.out.println("\nCartas jugadas por " + jugadorMano.getNombre() + ": " + jugadasMano);
        System.out.println("Cartas jugadas por " + jugadorPie.getNombre() + ": " + jugadasPie + "\n");
    }

    // Registra la carta jugada por un jugador en el mapa general
    public void registrarJugada(Jugador jugador, Carta carta) {
        jugadas.putIfAbsent(jugador, new ArrayList<>());
        jugadas.get(jugador).add(carta);
    }

    // Forzar el turno de un jugador (por ejemplo, si se acepta Truco)
    public void forzarTurno(Jugador jugador) {
        setJugadorActual(jugador);
    }

    // Alterna turno sin verificar lógica de orden
    public void pasarTurno() {
        jugadorActual = (jugadorActual == jugadorMano) ? jugadorPie : jugadorMano;
    }

    // Enum para definir el resultado de cada mano
    private enum ResultadoParcial {
        GANA_MANO,
        GANA_PIE,
        PARDA
    }

    // Permite ejecutar jugadas directamente (por interfaz o simulación)
    public void jugarTurnoConCarta(Jugador jugador, Carta carta) {
        if (rondaFinalizada) return;

        if (jugador == jugadorMano) {
            jugadasMano.add(carta);
        } else {
            jugadasPie.add(carta);
        }

        registrarJugada(jugador, carta);

        if (jugadasMano.size() > jugadasPie.size()) {
            ordenJugadoresPrimeros.add(jugadorMano);
        } else if (jugadasPie.size() > jugadasMano.size()) {
            ordenJugadoresPrimeros.add(jugadorPie);
        }

        if (jugadasMano.size() == jugadasPie.size()) {
            evaluarMano(jugadasMano.size() - 1);

            if (verificarCierreAnticipado()) {
                rondaFinalizada = true;
                return;
            }

            if (jugadasMano.size() == 3 && jugadasPie.size() == 3) {
                rondaFinalizada = true;
            }

            actualizarJugadorActual();
        } else {
            cambiarTurno();
        }

        this.ultimoJugadorQueJugo = jugador;
    }

    // Devuelve las cartas jugadas por el jugador humano
    public List<Carta> getCartasJugadasHumano() {
        return jugadorMano instanceof Truco.jugadores.JugadorHumano ? jugadasMano : jugadasPie;
    }

}
