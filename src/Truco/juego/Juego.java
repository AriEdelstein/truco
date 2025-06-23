package Truco.juego;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;
import Truco.cartas.Mazo;
import Truco.cantos.Envido;
import Truco.cantos.Truco;
import Truco.jugadores.*;
import java.util.Scanner;

public class Juego {
    // Jugadores
    private final Jugador jugador1;
    private final Jugador jugador2;
    // Controlador de puntos
    private final Puntuacion puntuacion;
    // Mazo de cartas
    private final Mazo mazo;
    // Jugador mano (empieza la ronda) y pie (va después)
    private Jugador mano;
    private Jugador pie;

    private final Scanner scanner = new Scanner(System.in);

    // Constructor: inicializa jugadores, puntuación y mazo
    public Juego(Jugador jugador1, Jugador jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.puntuacion = new Puntuacion(jugador1, jugador2);
        this.mazo = new Mazo();
        this.mano = jugador1;
        this.pie = jugador2;
    }

    // Método principal del juego, ejecuta rondas hasta que haya un ganador
    public void jugar() {
        while (!puntuacion.hayGanador()) {
            System.out.println("\n--- NUEVA RONDA ---");
            prepararRonda(); // Baraja y reparte cartas
            Ronda ronda = new Ronda(mano, pie); // Crea una nueva ronda

            if (jugador1 instanceof JugadorHumano) {
                System.out.println("\nTus cartas:");
                jugador1.mostrarCartas(); // Muestra cartas al humano
            }

            // === CANTO DE ENVIDO ===
            Envido envido = new Envido();
            envido.reiniciar();

            boolean envidoOfrecido = false;

            // Solo el jugador mano puede cantar Envido primero
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
                    // Turnos alternados hasta que se acepte o rechace
                    while (!envido.estaAceptado() && !envido.estaRechazado()) {
                        Jugador respuesta = obtenerRival(actual);

                        // Si responde un humano, muestra opciones
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
                            // CPU decide si aceptar o no según su estrategia
                            if (cpu.deseaAceptarEnvido(nivel, tipo.ordinal())) {
                                envido.aceptar();
                            } else {
                                envido.rechazar();
                                puntuacion.sumarPuntos(actual, envido.puntosSiNoSeAcepta());
                            }
                        }

                        actual = obtenerRival(actual);
                    }

                    // Si se aceptó el Envido, se calculan valores y puntos
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

            // Si el pie es humano y la CPU no cantó Envido, puede hacerlo ahora
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
                            // mismas opciones que antes
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

            // === CANTO DE TRUCO ===
            Truco truco = new Truco();
            boolean rondaCancelada = false;
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

                        // Posibilidad de subir a Retruco
                        if (preguntarAceptacionHumano(turno, "¿Querés subir a Retruco?")) {
                            truco.subirTruco();
                            if (rival instanceof JugadorCPU cpu && !cpu.deseaAceptarRetruco()) {
                                truco.rechazar();
                                puntuacion.sumarPuntos(turno, truco.puntosSiNoSeAcepta());
                                rondaCancelada = true;
                                break;
                            }
                            truco.aceptar();

                            // Posibilidad de que CPU suba a Vale Cuatro
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

                        if (truco.estaEnCurso() && truco.fueCantadoPor(cpu) && turno instanceof JugadorHumano && !yaPreguntadoSubirRetruco) {
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

                ronda.jugarTurno(); // Se juega el turno actual
            }

            if (!rondaCancelada) {
                ronda.mostrarJugadas();
                Jugador ganadorRonda = ronda.determinarGanador();
                int puntosTruco = truco.estaEnCurso() ? truco.puntosSiSeAcepta() : 1;
                puntuacion.sumarPuntos(ganadorRonda, puntosTruco);
            }

            puntuacion.mostrarPuntajes(); // Muestra puntuaciones actualizadas
            cambiarMano(); // Alterna quién es mano para la próxima ronda
        }

        // Cuando hay un ganador, termina el juego
        Jugador ganador = puntuacion.obtenerGanador();
        System.out.println("🏆 ¡" + ganador.getNombre() + " ganó la partida con " + ganador.getPuntos() + " puntos!");
    }

    // Pregunta a un jugador humano si acepta el canto
    private boolean preguntarAceptacionHumano(Jugador jugador, String mensaje) {
        String r;
        do {
            if (mensaje.trim().endsWith("?")) {
                System.out.print(jugador.getNombre() + ", " + mensaje + " (s/n): ");
            } else {
                System.out.print(jugador.getNombre() + ", ¿Aceptás " + mensaje + "? (s/n): ");
            }
            r = scanner.nextLine().trim().toLowerCase();
        } while (!r.equals("s") && !r.equals("n"));
        return r.equals("s");
    }

    // Reparte cartas a ambos jugadores
    private void prepararRonda() {
        mazo.reiniciar();
        mano.setCartas(mazo.repartirCartas(3));
        pie.setCartas(mazo.repartirCartas(3));
        mano.setEsMano(true);
        pie.setEsMano(false);
    }

    // Cambia quién es mano y pie
    private void cambiarMano() {
        Jugador temp = mano;
        mano = pie;
        pie = temp;
    }

    // Devuelve el rival del jugador pasado
    private Jugador obtenerRival(Jugador jugador) {
        return jugador == jugador1 ? jugador2 : jugador1;
    }

    // Determina el ganador del Envido
    private Jugador calcularGanadorEnvido(int e1, int e2) {
        if (e1 == e2) {
            return jugador1.esMano() ? jugador1 : jugador2;
        }
        return (e1 > e2) ? jugador1 : jugador2;
    }

    // Devuelve el jugador que debe jugar el turno actual
    private Jugador obtenerJugadorTurno(Ronda ronda) {
        return ronda.getJugadorActual();
    }

    public Mazo getMazo() {
        return mazo;
    }
}
