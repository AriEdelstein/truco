package Truco.juego;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;
import Truco.cartas.Mazo;
import Truco.cantos.Envido;
import Truco.cantos.Truco;
import Truco.jugadores.*;
import java.util.Scanner;

public class Juego {
    private final Jugador jugador1;
    private final Jugador jugador2;
    private final Puntuacion puntuacion;
    private final Mazo mazo;
    private Jugador mano;
    private Jugador pie;

    private final Scanner scanner = new Scanner(System.in);

    public Juego(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.puntuacion = new Puntuacion(jugador1, jugador2);
        this.mazo = new Mazo();
        this.mano = jugador1;
        this.pie = jugador2;
    }

    public void jugar() {
        while (!puntuacion.hayGanador()) {
            System.out.println("\n--- NUEVA RONDA ---");
            prepararRonda();
            Ronda ronda = new Ronda(mano, pie);

            if (jugador1 instanceof JugadorHumano) {
                System.out.println("\nTus cartas:");
                jugador1.mostrarCartas();
            }

            // === ENVIDO (prioridad para el jugador mano) ===
            Envido envido = new Envido();
            envido.reiniciar();

            boolean envidoOfrecido = false;

            // Solo el jugador mano puede ofrecer envido primero
            if (mano instanceof Cantante cantanteMano) {
                NivelEnvido tipo = cantanteMano.deseaCantarEnvido();
                if (tipo != NivelEnvido.NINGUNO) {
                    envidoOfrecido = true;
                    switch (tipo) {
                        case ENVIDO -> envido.cantarEnvido();
                        case REAL_ENVIDO -> envido.subirRealEnvido();
                        case FALTA_ENVIDO -> envido.subirFaltaEnvido();
                    }

                    Jugador actual = mano;
                    while (!envido.estaAceptado() && !envido.estaRechazado()) {
                        Jugador respuesta = obtenerRival(actual);

                        if (respuesta instanceof JugadorHumano) {
                            System.out.println("\nOpciones para responder Envido:");
                            System.out.println("1. Quiero");
                            System.out.println("2. No quiero");
                            if (!envido.esFaltaEnvido()) {
                                if (!envido.esRealEnvido()) System.out.println("3. Envido");
                                System.out.println("4. Real Envido");
                            }
                            System.out.println("5. Falta Envido");
                            System.out.print("Elegí una opción: ");

                            String opcion = scanner.nextLine().trim();
                            switch (opcion) {
                                case "1" -> envido.aceptar();
                                case "2" -> {
                                    envido.rechazar();
                                    puntuacion.sumarPuntos(actual, envido.puntosSiNoSeAcepta());
                                }
                                case "3" -> envido.subirEnvido();
                                case "4" -> envido.subirRealEnvido();
                                case "5" -> envido.subirFaltaEnvido();
                            }
                        } else if (respuesta instanceof JugadorCPU cpu) {
                            int nivel = envido.getNivelEnvido();
                            if (cpu.deseaAceptarEnvido(nivel, tipo.ordinal())) {
                                envido.aceptar();
                            } else {
                                envido.rechazar();
                                puntuacion.sumarPuntos(actual, envido.puntosSiNoSeAcepta());
                            }
                        }

                        actual = obtenerRival(actual);
                    }

                    if (envido.estaAceptado()) {
                        int e1 = envido.calcularEnvido(jugador1.getCartas());
                        int e2 = envido.calcularEnvido(jugador2.getCartas());

                        System.out.println(jugador1.getNombre() + " tiene " + e1 + " de envido.");
                        System.out.println(jugador2.getNombre() + " tiene " + e2 + " de envido.");

                        Jugador ganadorEnvido = calcularGanadorEnvido(e1, e2);
                        System.out.println("🏅 Gana el envido: " + ganadorEnvido.getNombre());

                        int puntos = envido.puntosGanados(ganadorEnvido, obtenerRival(ganadorEnvido));
                        puntuacion.sumarPuntos(ganadorEnvido, puntos);
                    }
                }
            }

            // Si la CPU no ofreció envido y el humano es pie, se le pregunta ahora
            if (!envidoOfrecido && pie instanceof JugadorHumano && pie instanceof Cantante cantantePie && mano instanceof JugadorCPU) {
                NivelEnvido tipo = cantantePie.deseaCantarEnvido();
                if (tipo != NivelEnvido.NINGUNO) {
                    switch (tipo) {
                        case ENVIDO -> envido.cantarEnvido();
                        case REAL_ENVIDO -> envido.subirRealEnvido();
                        case FALTA_ENVIDO -> envido.subirFaltaEnvido();
                    }

                    Jugador actual = pie;
                    while (!envido.estaAceptado() && !envido.estaRechazado()) {
                        Jugador respuesta = obtenerRival(actual);

                        if (respuesta instanceof JugadorHumano) {
                            System.out.println("\nOpciones para responder Envido:");
                            System.out.println("1. Quiero");
                            System.out.println("2. No quiero");
                            if (!envido.esFaltaEnvido()) {
                                if (!envido.esRealEnvido()) System.out.println("3. Envido");
                                System.out.println("4. Real Envido");
                            }
                            System.out.println("5. Falta Envido");
                            System.out.print("Elegí una opción: ");

                            String opcion = scanner.nextLine().trim();
                            switch (opcion) {
                                case "1" -> envido.aceptar();
                                case "2" -> {
                                    envido.rechazar();
                                    puntuacion.sumarPuntos(actual, envido.puntosSiNoSeAcepta());
                                }
                                case "3" -> envido.subirEnvido();
                                case "4" -> envido.subirRealEnvido();
                                case "5" -> envido.subirFaltaEnvido();
                            }
                        } else if (respuesta instanceof JugadorCPU cpu) {
                            int nivel = envido.getNivelEnvido();
                            if (cpu.deseaAceptarEnvido(nivel, tipo.ordinal())) {
                                envido.aceptar();
                            } else {
                                envido.rechazar();
                                puntuacion.sumarPuntos(actual, envido.puntosSiNoSeAcepta());
                            }
                        }

                        actual = obtenerRival(actual);
                    }

                    if (envido.estaAceptado()) {
                        int e1 = envido.calcularEnvido(jugador1.getCartas());
                        int e2 = envido.calcularEnvido(jugador2.getCartas());

                        System.out.println(jugador1.getNombre() + " tiene " + e1 + " de envido.");
                        System.out.println(jugador2.getNombre() + " tiene " + e2 + " de envido.");

                        Jugador ganadorEnvido = calcularGanadorEnvido(e1, e2);
                        System.out.println("🏅 Gana el envido: " + ganadorEnvido.getNombre());

                        int puntos = envido.puntosGanados(ganadorEnvido, obtenerRival(ganadorEnvido));
                        puntuacion.sumarPuntos(ganadorEnvido, puntos);
                    }
                }
            }



            // === TRUCO ===
            Truco truco = new Truco();
            boolean rondaCancelada = false;
            boolean humanoYaPreguntoTruco = false;
            boolean yaPreguntadoSubirRetruco = false;

            while (!ronda.rondaCompleta() && !rondaCancelada) {
                Jugador turno = obtenerJugadorTurno(ronda);

                if (!truco.estaEnCurso() && turno instanceof Cantante cantante && cantante.deseaCantarTruco()) {
                    truco.cantarTruco(turno);
                    Jugador rival = obtenerRival(turno);

                    if (rival instanceof JugadorHumano) {
                        if (!preguntarAceptacionHumano(rival, "Truco")) {
                            truco.rechazar();
                            puntuacion.sumarPuntos(turno, truco.puntosSiNoSeAcepta());
                            rondaCancelada = true;
                            break;
                        }
                        truco.aceptar();

                        if (preguntarAceptacionHumano(turno, "¿Querés subir a Retruco?")) {
                            truco.subirTruco();
                            if (rival instanceof JugadorCPU cpu && !cpu.deseaAceptarRetruco()) {
                                truco.rechazar();
                                puntuacion.sumarPuntos(turno, truco.puntosSiNoSeAcepta());
                                rondaCancelada = true;
                                break;
                            }
                            truco.aceptar();

                            if (rival instanceof JugadorCPU cpu && cpu.deseaCantarValeCuatro()) {
                                truco.subirTruco();
                                if (!preguntarAceptacionHumano(turno, "Vale Cuatro")) {
                                    truco.rechazar();
                                    puntuacion.sumarPuntos(rival, truco.puntosSiNoSeAcepta());
                                    rondaCancelada = true;
                                    break;
                                }
                                truco.aceptar();
                            }
                        }

                    } else if (rival instanceof JugadorCPU cpu) {
                        if (!cpu.deseaAceptarTruco()) {
                            truco.rechazar();
                            puntuacion.sumarPuntos(turno, truco.puntosSiNoSeAcepta());
                            rondaCancelada = true;
                            break;
                        }
                        truco.aceptar();
                        ronda.forzarTurno(turno);

                        if (truco.estaEnCurso() && truco.fueCantadoPorCPU() && turno instanceof JugadorHumano && !yaPreguntadoSubirRetruco) {
                            yaPreguntadoSubirRetruco = true;
                            if (preguntarAceptacionHumano(turno, "¿Querés subir a Retruco?")) {
                                truco.subirTruco();
                                if (!cpu.deseaAceptarRetruco()) {
                                    truco.rechazar();
                                    puntuacion.sumarPuntos(turno, truco.puntosSiNoSeAcepta());
                                    rondaCancelada = true;
                                    break;
                                }
                                truco.aceptar();

                                if (cpu.deseaCantarValeCuatro()) {
                                    truco.subirTruco();
                                    if (!preguntarAceptacionHumano(turno, "Vale Cuatro")) {
                                        truco.rechazar();
                                        puntuacion.sumarPuntos(cpu, truco.puntosSiNoSeAcepta());
                                        rondaCancelada = true;
                                        break;
                                    }
                                    truco.aceptar();
                                }
                            }
                        }
                        continue;
                    }
                }

                ronda.jugarTurno();
            }

            if (!rondaCancelada) {
                ronda.mostrarJugadas();
                Jugador ganadorRonda = ronda.determinarGanador();
                int puntosTruco = truco.estaEnCurso() ? truco.puntosSiSeAcepta() : 1;
                puntuacion.sumarPuntos(ganadorRonda, puntosTruco);
            }

            puntuacion.mostrarPuntajes();
            cambiarMano();
        }

        Jugador ganador = puntuacion.obtenerGanador();
        System.out.println("🏆 ¡" + ganador.getNombre() + " ganó la partida con " + ganador.getPuntos() + " puntos!");
    }

    private boolean preguntarAceptacionHumano(Jugador jugador, String mensaje) {
        String r;
        do {
            // Si el mensaje ya contiene una pregunta, mostralo tal cual
            if (mensaje.trim().endsWith("?")) {
                System.out.print(jugador.getNombre() + ", " + mensaje + " (s/n): ");
            } else {
                System.out.print(jugador.getNombre() + ", ¿Aceptás " + mensaje + "? (s/n): ");
            }
            r = scanner.nextLine().trim().toLowerCase();
        } while (!r.equals("s") && !r.equals("n"));
        return r.equals("s");
    }


    private void prepararRonda() {
        mazo.reiniciar();
        mano.setCartas(mazo.repartirCartas(3));
        pie.setCartas(mazo.repartirCartas(3));
        mano.setEsMano(true);
        pie.setEsMano(false);
    }

    private void cambiarMano() {
        Jugador temp = mano;
        mano = pie;
        pie = temp;
    }

    private Jugador obtenerRival(Jugador jugador) {
        return jugador == jugador1 ? jugador2 : jugador1;
    }

    private Jugador calcularGanadorEnvido(int e1, int e2) {
        if (e1 == e2) {
            return jugador1.esMano() ? jugador1 : jugador2;
        }
        return (e1 > e2) ? jugador1 : jugador2;
    }

    private Jugador obtenerJugadorTurno(Ronda ronda) {
        return ronda.getJugadorActual();
    }
}
