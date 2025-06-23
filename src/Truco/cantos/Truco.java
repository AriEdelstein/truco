package Truco.cantos;

import Truco.jugadores.Jugador;
import Truco.jugadores.JugadorCPU;

public class Truco {
    // Nivel actual del canto de Truco (TRUCO, RETRUCO, VALECUATRO, etc.)
    private NivelTruco nivelActual;
    private boolean enCurso;             // Indica si el canto de Truco está en curso
    private boolean aceptado;            // Indica si el Truco fue aceptado
    private boolean rechazado;           // Indica si el Truco fue rechazado
    private Jugador jugadorQueCanto;     // Jugador que inició el canto de Truco

    public Truco() {
        reiniciar(); // Inicializa los valores al estado por defecto
    }

    // Método para cantar Truco
    public void cantarTruco(Jugador jugador) {
        if (!enCurso) { // Solo se puede cantar si no hay uno en curso
            nivelActual = NivelTruco.TRUCO; // Se establece el primer nivel del Truco
            enCurso = true;
            aceptado = false;
            rechazado = false;
            jugadorQueCanto = jugador; // Se guarda quién lo cantó
            System.out.println("⚔️ Se canta TRUCO.");
        }
    }

    // Verifica si el Truco fue cantado por el jugador pasado por parámetro
    public boolean fueCantadoPor(Jugador jugador) {
        return jugadorQueCanto == jugador;
    }

    // Método para subir el nivel del Truco (TRUCO → RETRUCO → VALECUATRO)
    public void subirTruco() {
        if (!enCurso || rechazado) {
            // No se puede subir si no está en curso o si fue rechazado
            System.out.println("⚠️ No se puede subir ahora.");
            return;
        }

        // Se sube el nivel del Truco usando switch con pattern matching (Java 17+)
        switch (nivelActual) {
            case TRUCO -> {
                nivelActual = NivelTruco.RETRUCO;
                aceptado = false; // Debe volver a ser aceptado
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

    // El jugador acepta el Truco
    public void aceptar() {
        this.aceptado = true;
        System.out.println("✅ Truco aceptado.");
    }

    // El jugador rechaza el Truco
    public void rechazar() {
        this.rechazado = true;
        this.enCurso = false; // Se termina el canto
        System.out.println("❌ Truco no aceptado.");
    }

    // Devuelve los puntos que se ganan si el Truco es aceptado
    public int puntosSiSeAcepta() {
        return switch (nivelActual) {
            case TRUCO -> 2;
            case RETRUCO -> 3;
            case VALECUATRO -> 4;
            default -> 1; // En caso de error o estado inválido
        };
    }

    // Devuelve los puntos que gana el que cantó si el Truco no es aceptado
    public int puntosSiNoSeAcepta() {
        return switch (nivelActual) {
            case TRUCO -> 1;
            case RETRUCO -> 2;
            case VALECUATRO -> 3;
            default -> 0;
        };
    }

    // Devuelve el nivel actual del Truco (TRUCO, RETRUCO, etc.)
    public NivelTruco getNivelActual() {
        return nivelActual;
    }

    // Indica si el Truco fue aceptado
    public boolean estaAceptado() {
        return aceptado;
    }

    // Indica si un jugador puede cantar Truco (si no está en curso ni rechazado)
    public boolean puedeCantar(Jugador jugador) {
        return !enCurso && !rechazado;
    }

    // Verifica si es posible subir el canto de Truco
    public boolean puedeSubir() {
        return enCurso && aceptado && nivelActual != NivelTruco.VALECUATRO;
    }

    // Devuelve el próximo nivel del Truco (TRUCO → RETRUCO → VALECUATRO)
    public NivelTruco getSiguienteNivel() {
        return switch (nivelActual) {
            case TRUCO -> NivelTruco.RETRUCO;
            case RETRUCO -> NivelTruco.VALECUATRO;
            default -> NivelTruco.VALECUATRO; // Por defecto
        };
    }

    // Verifica si el Truco fue rechazado
    public boolean estaRechazado() {
        return rechazado;
    }

    // Verifica si el Truco está en curso
    public boolean estaEnCurso() {
        return enCurso;
    }

    // Reinicia todos los estados del objeto Truco
    public void reiniciar() {
        nivelActual = NivelTruco.NINGUNO;
        enCurso = false;
        aceptado = false;
        rechazado = false;
    }

    // Verifica si un jugador puede subir el Truco
    public boolean puedeSubirTruco(Jugador jugador) {
        // Solo se puede subir si está en curso, fue aceptado, no está al máximo, y el jugador no fue el que lo cantó
        return enCurso && aceptado && nivelActual != NivelTruco.VALECUATRO && jugador != jugadorQueCanto;
    }

}
