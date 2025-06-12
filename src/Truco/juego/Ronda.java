package Truco.juego;

import Truco.cartas.Carta;
import Truco.jugadores.Jugador;
import Truco.util.Utils;

import java.util.*;

public class Ronda {
    private final Jugador jugadorMano;
    private final Jugador jugadorPie;

    private final List<Carta> jugadasMano = new ArrayList<>();
    private final List<Carta> jugadasPie = new ArrayList<>();
    private final List<ResultadoParcial> resultadosManos = new ArrayList<>();
    private final Map<Jugador, List<Carta>> jugadas = new HashMap<>();

    private Jugador jugadorActual;
    private Jugador otroJugador;

    private final List<Jugador> ordenJugadoresPrimeros = new ArrayList<>();
    private boolean rondaFinalizada;

    public Ronda(Jugador jugadorMano, Jugador jugadorPie) {
        this.jugadorMano = jugadorMano;
        this.jugadorPie = jugadorPie;
        this.jugadorActual = jugadorMano;
        this.otroJugador = jugadorPie;
    }

    public void jugarTurno() {
        if (rondaFinalizada) return;

        // No permitir que un jugador juegue más cartas que el otro (desfase)
        if (jugadorActual == jugadorMano && jugadasMano.size() > jugadasPie.size()) return;
        if (jugadorActual == jugadorPie && jugadasPie.size() > jugadasMano.size()) return;

        Carta jugada = jugadorActual.jugarCarta();
        System.out.println("---------------------------------------------");
        System.out.println(jugadorActual.getNombre() + " jugó: " + jugada);
        System.out.println("---------------------------------------------");

        if (jugadorActual == jugadorMano) {
            jugadasMano.add(jugada);
        } else {
            jugadasPie.add(jugada);
        }

        registrarJugada(jugadorActual, jugada);

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

            actualizarJugadorActual();
        } else {
            cambiarTurno();
        }
    }

    private void evaluarMano(int index) {
        Carta cartaMano = jugadasMano.get(index);
        Carta cartaPie = jugadasPie.get(index);

        int cmp = Utils.compararCartasTruco(cartaMano, cartaPie);
        if (cmp < 0) {
            resultadosManos.add(ResultadoParcial.GANA_PIE);
        } else if (cmp > 0) {
            resultadosManos.add(ResultadoParcial.GANA_MANO);
        } else {
            resultadosManos.add(ResultadoParcial.PARDA);
        }
    }
    public List<Carta> getCartasJugadas(Jugador jugador) {
        if (jugador == jugadorMano) return jugadasMano;
        if (jugador == jugadorPie) return jugadasPie;
        return new ArrayList<>();
    }


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

    private void cambiarTurno() {
        setJugadorActual(otroJugador);
    }

    private void setJugadorActual(Jugador nuevoActual) {
        jugadorActual = nuevoActual;
        otroJugador = (nuevoActual == jugadorMano) ? jugadorPie : jugadorMano;
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }


    public boolean rondaCompleta() {
        return rondaFinalizada || (jugadasMano.size() == 3 && jugadasPie.size() == 3);
    }

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

        return (ganadasMano == ganadasPie) ? jugadorMano : (ganadasMano > ganadasPie ? jugadorMano : jugadorPie);
    }

    private boolean verificarCierreAnticipado() {
        if (resultadosManos.size() < 2) return false;

        ResultadoParcial r1 = resultadosManos.get(0);
        ResultadoParcial r2 = resultadosManos.get(1);

        if (r1 == ResultadoParcial.PARDA) {
            return r2 == ResultadoParcial.GANA_MANO || r2 == ResultadoParcial.GANA_PIE;
        }

        return r1 == r2 && r1 != ResultadoParcial.PARDA;
    }

    public void mostrarJugadas() {
        System.out.println("\nCartas jugadas por " + jugadorMano.getNombre() + ": " + jugadasMano);
        System.out.println("Cartas jugadas por " + jugadorPie.getNombre() + ": " + jugadasPie + "\n");
    }

    public void registrarJugada(Jugador jugador, Carta carta) {
        jugadas.putIfAbsent(jugador, new ArrayList<>());
        jugadas.get(jugador).add(carta);
    }

    public Carta obtenerUltimaCartaJugada(Jugador jugador) {
        List<Carta> cartas = jugadas.get(jugador);
        if (cartas != null && !cartas.isEmpty()) {
            return cartas.get(cartas.size() - 1);
        }
        return null;
    }

    public void forzarTurno(Jugador jugador) {
        setJugadorActual(jugador);
    }

    // Enum interno
    private enum ResultadoParcial {
        GANA_MANO,
        GANA_PIE,
        PARDA
    }
}
