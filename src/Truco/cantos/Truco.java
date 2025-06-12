package Truco.cantos;

import Truco.jugadores.Jugador;
import Truco.jugadores.JugadorCPU;

public class Truco {
    private NivelTruco nivelActual;
    private boolean enCurso;
    private boolean aceptado;
    private boolean rechazado;
    private Jugador jugadorQueCanto;

    public Truco() {
        reiniciar();
    }

    public void cantarTruco(Jugador jugador) {
        if (!enCurso) {
            nivelActual = NivelTruco.TRUCO;
            enCurso = true;
            aceptado = false;
            rechazado = false;
            jugadorQueCanto = jugador;
            System.out.println("⚔️ Se canta TRUCO.");
        }
    }

    public boolean fueCantadoPorCPU() {
        return jugadorQueCanto instanceof JugadorCPU;
    }


    public void subirTruco() {
        if (!enCurso || rechazado || !aceptado) {
            System.out.println("⚠️ No se puede subir ahora.");
            return;
        }

        switch (nivelActual) {
            case TRUCO -> {
                nivelActual = NivelTruco.RETRUCO;
                aceptado = false;
                System.out.println("⚔️ Se sube a RETRUCO.");
            }
            case RETRUCO -> {
                nivelActual = NivelTruco.VALECUATRO;
                aceptado = false;
                System.out.println("⚔️ Se sube a VALE CUATRO.");
            }
            case VALECUATRO -> System.out.println("⚠️ Ya estamos en el máximo nivel de Truco.");
            default -> System.out.println("⚠️ Truco no iniciado.");
        }
    }

    public void aceptar() {
        this.aceptado = true;
        System.out.println("✅ Truco aceptado.");
    }

    public void rechazar() {
        this.rechazado = true;
        System.out.println("❌ Truco no aceptado.");
    }

    public int puntosSiSeAcepta() {
        return switch (nivelActual) {
            case TRUCO -> 2;
            case RETRUCO -> 3;
            case VALECUATRO -> 4;
            default -> 1;
        };
    }

    public int puntosSiNoSeAcepta() {
        return switch (nivelActual) {
            case TRUCO -> 1;
            case RETRUCO -> 2;
            case VALECUATRO -> 3;
            default -> 0;
        };
    }

    public NivelTruco getNivelActual() {
        return nivelActual;
    }

    public boolean estaAceptado() {
        return aceptado;
    }
    public boolean puedeCantar(Jugador jugador) {
        return !enCurso && !rechazado;
    }

    public boolean puedeSubir() {
        return enCurso && aceptado && nivelActual != NivelTruco.VALECUATRO;
    }

    public NivelTruco getSiguienteNivel() {
        return switch (nivelActual) {
            case TRUCO -> NivelTruco.RETRUCO;
            case RETRUCO -> NivelTruco.VALECUATRO;
            default -> NivelTruco.VALECUATRO; // Por defecto para evitar null
        };
    }

    public boolean estaRechazado() {
        return rechazado;
    }

    public boolean estaEnCurso() {
        return enCurso;
    }

    public void reiniciar() {
        nivelActual = NivelTruco.NINGUNO;
        enCurso = false;
        aceptado = false;
        rechazado = false;
    }
}
