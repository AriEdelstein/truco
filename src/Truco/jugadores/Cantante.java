package Truco.jugadores;

import Truco.cantos.NivelEnvido;

public interface Cantante {

    // Envido: 0 = no canta, 1 = envido, 2 = real envido, 3 = falta envido
    NivelEnvido deseaCantarEnvido();

    // Truco: ¿quiere cantar truco?
    boolean deseaCantarTruco();

    // Por defecto: acepta todo, JugadorCPU puede sobreescribir estos métodos.

    default boolean deseaAceptarTruco() {
        return true;
    }

    default boolean deseaCantarRetruco() {
        return false;
    }

    default boolean deseaAceptarRetruco() {
        return true;
    }

    default boolean deseaCantarValeCuatro() {
        return false;
    }

    default boolean deseaAceptarValeCuatro() {
        return true;
    }
}
