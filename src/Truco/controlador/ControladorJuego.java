package Truco.controlador;

import Truco.cantos.Envido;
import Truco.cantos.NivelEnvido;
import Truco.cantos.NivelTruco;
import Truco.cantos.Truco;
import Truco.cartas.Carta;
import Truco.juego.Juego;
import Truco.juego.Ronda;
import Truco.jugadores.Jugador;
import Truco.jugadores.JugadorCPU;
import Truco.jugadores.JugadorHumano;
import Truco.vista.PanelAcciones;
import Truco.vista.PanelCartasJugador;
import Truco.vista.PanelMensajes;
import Truco.vista.PanelPuntuacion;

import javax.swing.*;

public class ControladorJuego {

    private final PanelCartasJugador panelCartas;
    private final PanelAcciones panelAcciones;
    private final PanelMensajes panelMensajes;
    private final PanelPuntuacion panelPuntuacion;

    private final JugadorHumano jugadorHumano;
    private final JugadorCPU jugadorCPU;
    private final Juego juego;

    private Ronda ronda;
    private final Envido envido = new Envido();
    private final Truco truco = new Truco();

    public ControladorJuego(PanelCartasJugador panelCartas, PanelAcciones panelAcciones,
                            PanelMensajes panelMensajes, PanelPuntuacion panelPuntuacion) {

        this.panelCartas = panelCartas;
        this.panelAcciones = panelAcciones;
        this.panelMensajes = panelMensajes;
        this.panelPuntuacion = panelPuntuacion;

        this.jugadorHumano = new JugadorHumano("Vos");
        this.jugadorCPU = new JugadorCPU("CPU");
        this.juego = new Juego(jugadorHumano, jugadorCPU);

        inicializarEventos();
        iniciarNuevaRonda();
    }

    private void inicializarEventos() {
        panelAcciones.setAccionTruco(e -> cantarTruco());
        panelAcciones.setAccionEnvido(e -> cantarEnvido());
        panelAcciones.setAccionMazo(e -> irseAlMazo());
        panelAcciones.setAccionSubirTruco(e -> subirTruco());

        for (int i = 0; i < 3; i++) {
            final int index = i;
            panelCartas.setAccionCarta(i, e -> jugarCarta(index));
        }
    }

    private void iniciarNuevaRonda() {
        // 👇 Limpia el estado visual anterior
        panelMensajes.limpiarMensajes();
        panelCartas.mostrarCartas(new String[]{"Sin carta", "Sin carta", "Sin carta"});

        juego.getMazo().reiniciar();
        jugadorHumano.setCartas(juego.getMazo().repartirCartas(3));
        jugadorCPU.setCartas(juego.getMazo().repartirCartas(3));

        jugadorHumano.setEsMano(!jugadorHumano.esMano());
        jugadorCPU.setEsMano(!jugadorCPU.esMano());

        Jugador mano = jugadorHumano.esMano() ? jugadorHumano : jugadorCPU;
        Jugador pie = jugadorHumano.esMano() ? jugadorCPU : jugadorHumano;

        ronda = new Ronda(mano, pie);
        truco.reiniciar();
        envido.reiniciar(); // 👈 AGREGADO: reiniciar el estado del envido

        panelMensajes.agregarMensaje("🆕 Nueva ronda. Mano: " + mano.getNombre());
        panelAcciones.habilitarAcciones(true);
        actualizarVista();

        if (ronda.getJugadorActual() instanceof JugadorCPU) {
            SwingUtilities.invokeLater(this::jugarTurnoCPU);
        } else {
            panelAcciones.habilitarBotonTruco(true);
        }
    }

    private void jugarCarta(int indice) {
        if (!(ronda.getJugadorActual() instanceof JugadorHumano)) return;
        if (indice >= jugadorHumano.getCartas().size()) return;

        Carta carta = jugadorHumano.jugarCartaDesdeGUI(indice);
        panelMensajes.agregarMensaje("🃏 Jugaste: " + carta);

        ronda.jugarTurnoConCarta(jugadorHumano, carta);

        panelAcciones.habilitarBotonEnvido(false);
        actualizarVista();
        verificarEstadoRonda();

        if (ronda.getJugadorActual() instanceof JugadorCPU) {
            SwingUtilities.invokeLater(this::jugarTurnoCPU);
        }
    }

    private void actualizarVista() {
        String[] nombres = jugadorHumano.getCartas().stream()
                .map(Carta::toString)
                .toArray(String[]::new);

        panelCartas.mostrarCartas(nombres);
        actualizarPuntuacion();

        // ✅ El botón debe habilitarse si el humano puede CANTAR o SUBIR el truco (Retruco o Vale Cuatro)
        boolean puedeCantar =
                truco.puedeCantar(jugadorHumano) ||
                        truco.puedeSubirTruco(jugadorHumano);  // <- Esto permite subir luego

        panelAcciones.habilitarBotonTruco(puedeCantar);

        //poder subir el truco
        boolean puedeSubirTruco = truco.puedeSubirTruco(jugadorHumano)
                && truco.estaEnCurso()
                && truco.estaAceptado();

        panelAcciones.habilitarBotonSubirTruco(puedeSubirTruco);
    }

    private void subirTruco() {
        truco.subirTruco();
        panelMensajes.agregarMensaje("🔥 Subiste a " + truco.getNivelActual());

        if (truco.getNivelActual() == NivelTruco.RETRUCO) {
            if (jugadorCPU.deseaAceptarRetruco()) {
                truco.aceptar();
                panelMensajes.agregarMensaje("🤖 CPU aceptó el RETRUCO");
            } else {
                truco.rechazar();
                jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
                panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
                panelAcciones.habilitarBotonSubirTruco(false);
                finalizarRonda();
                return;
            }

        } else if (truco.getNivelActual() == NivelTruco.VALECUATRO) {
            if (jugadorCPU.deseaAceptarValeCuatro()) {
                truco.aceptar();
                panelMensajes.agregarMensaje("🤖 CPU aceptó el VALE CUATRO");
                panelAcciones.habilitarBotonSubirTruco(false); // No se puede subir más
            } else {
                truco.rechazar();
                jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
                panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
                panelAcciones.habilitarBotonSubirTruco(false);
                finalizarRonda();
                return;
            }
        }

        actualizarVista();
    }



    private void actualizarPuntuacion() {
        panelPuntuacion.actualizarPuntos(jugadorHumano.getPuntos(), jugadorCPU.getPuntos());
    }

    private void cantarTruco() {
        if (!truco.puedeCantar(jugadorHumano)) return;

        truco.cantarTruco(jugadorHumano);
        panelMensajes.agregarMensaje("✋ Cantaste Truco");
        panelAcciones.habilitarBotonTruco(false);

        if (jugadorCPU.deseaAceptarTruco()) {
            truco.aceptar();
            panelMensajes.agregarMensaje("🤖 CPU aceptó el Truco");

            // CPU evalúa si quiere subir a Retruco
            if (jugadorCPU.deseaCantarRetruco()) {
                truco.subirTruco(); // Subir a Retruco
                panelMensajes.agregarMensaje("🤖 CPU sube a RETRUCO");

                // HUMANO decide si acepta o no el retruco
                boolean aceptaRetruco = jugadorHumano.deseaAceptarRetruco();
                if (aceptaRetruco) {
                    truco.aceptar();
                    panelMensajes.agregarMensaje("✅ Aceptaste el RETRUCO");

                    // CPU puede subir a VALE CUATRO después del retruco
                    if (jugadorCPU.deseaCantarValeCuatro()) {
                        truco.subirTruco(); // Subir a Vale Cuatro
                        panelMensajes.agregarMensaje("🤖 CPU sube a VALE CUATRO");

                        boolean aceptaValeCuatro = jugadorHumano.deseaAceptarValeCuatro();
                        if (aceptaValeCuatro) {
                            truco.aceptar();
                            panelMensajes.agregarMensaje("✅ Aceptaste el VALE CUATRO");
                        } else {
                            truco.rechazar();
                            panelMensajes.agregarMensaje("❌ No aceptaste el VALE CUATRO. CPU gana " + truco.puntosSiNoSeAcepta() + " punto(s).");
                            jugadorCPU.sumarPuntos(truco.puntosSiNoSeAcepta());
                            finalizarRonda();
                        }
                    }

                } else {
                    truco.rechazar();
                    panelMensajes.agregarMensaje("❌ No aceptaste el RETRUCO. CPU gana " + truco.puntosSiNoSeAcepta() + " punto(s).");
                    jugadorCPU.sumarPuntos(truco.puntosSiNoSeAcepta());
                    finalizarRonda();
                }
            }

        } else {
            truco.rechazar();
            panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
            jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
            finalizarRonda();
        }

        actualizarPuntuacion();
    }

    private void cantarEnvido() {
        String[] opciones = {"Envido", "Real Envido", "Falta Envido", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(null,
                "¿Qué querés cantar?", "Cantar Envido",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        NivelEnvido nivel = switch (seleccion) {
            case 0 -> NivelEnvido.ENVIDO;
            case 1 -> NivelEnvido.REAL_ENVIDO;
            case 2 -> NivelEnvido.FALTA_ENVIDO;
            default -> NivelEnvido.NINGUNO;
        };

        if (nivel == NivelEnvido.NINGUNO) return;

        switch (nivel) {
            case ENVIDO -> envido.cantarEnvido();
            case REAL_ENVIDO -> envido.subirRealEnvido();
            case FALTA_ENVIDO -> envido.subirFaltaEnvido();
        }

        panelMensajes.agregarMensaje("✋ Cantaste " + nivel);
        panelAcciones.habilitarBotonEnvido(false);

        boolean aceptado = jugadorCPU.deseaAceptarEnvido(envido.getNivelEnvido(), nivel.ordinal());

        if (aceptado) {
            envido.aceptar();
            panelMensajes.agregarMensaje("🤖 CPU aceptó el Envido");

            int eHumano = envido.calcularEnvido(jugadorHumano.getCartas());
            int eCPU = envido.calcularEnvido(jugadorCPU.getCartas());

            panelMensajes.agregarMensaje("📊 Tenés " + eHumano + " de envido.");
            panelMensajes.agregarMensaje("🤖 CPU tiene " + eCPU + " de envido.");

            Jugador ganador = (eHumano > eCPU || (eHumano == eCPU && jugadorHumano.esMano()))
                    ? jugadorHumano : jugadorCPU;

            int puntos = envido.puntosGanados(ganador, (ganador == jugadorHumano) ? jugadorCPU : jugadorHumano);
            ganador.sumarPuntos(puntos);
            panelMensajes.agregarMensaje("🏆 " + ganador.getNombre() + " gana el envido por " + puntos + " puntos.");
        } else {
            envido.rechazar();
            jugadorHumano.sumarPuntos(envido.puntosSiNoSeAcepta());
            panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás 1 punto.");
        }

        envido.reiniciar();
        actualizarPuntuacion();

        if (truco.fueCantadoPor(jugadorCPU) && truco.estaEnCurso() && !truco.estaAceptado() && !truco.estaRechazado()) {
            SwingUtilities.invokeLater(this::mostrarTrucoPendienteCPU); // 👈 vuelve a mostrar el cartel de Truco
        }

        // 🔁 Retomar Truco si quedó pendiente y fue cantado por la CPU
        if (truco.fueCantadoPor(jugadorCPU)
                && truco.estaEnCurso()
                && !truco.estaAceptado()
                && !truco.estaRechazado()) {
            SwingUtilities.invokeLater(this::jugarTurnoCPU);
        }
    }

    private void mostrarTrucoPendienteCPU() {
        if (truco.fueCantadoPor(jugadorCPU)
                && truco.estaEnCurso()
                && !truco.estaAceptado()
                && !truco.estaRechazado()) {

            int opcion = JOptionPane.showOptionDialog(null,
                    "CPU cantó Truco. ¿Qué querés hacer?",
                    "Respuesta al Truco",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"No quiero", "Quiero", "Retruco"},
                    "Quiero");

            if (opcion == JOptionPane.CLOSED_OPTION) {
                panelMensajes.agregarMensaje("❗ Cerraste el cartel. El Truco queda pendiente.");
                return;
            }

            if (opcion == 0) {
                truco.rechazar();
                jugadorCPU.sumarPuntos(truco.puntosSiNoSeAcepta());
                panelMensajes.agregarMensaje("🙅‍♂️ No quisiste. CPU gana " + truco.puntosSiNoSeAcepta() + " punto(s).");
                finalizarRonda();
            } else if (opcion == 1) {
                truco.aceptar();
                panelMensajes.agregarMensaje("✅ Aceptaste el Truco.");
                actualizarPuntuacion();
                actualizarVista();

                SwingUtilities.invokeLater(this::jugarTurnoCPU);
            } else if (opcion == 2) {
                truco.subirTruco();
                panelMensajes.agregarMensaje("🔥 Subiste a Retruco.");

                if (jugadorCPU.deseaAceptarRetruco()) {
                    truco.aceptar();
                    panelMensajes.agregarMensaje("🤖 CPU aceptó el Retruco.");
                    actualizarPuntuacion();
                } else {
                    truco.rechazar();
                    jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
                    panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
                    finalizarRonda();
                }
            }
        }
    }


    private void jugarTurnoCPU() {
        if (!(ronda.getJugadorActual() instanceof JugadorCPU)) return;

        if (!envido.estaEnCurso() && truco.getNivelActual() == null) {
            NivelEnvido nivelCPU = jugadorCPU.deseaCantarEnvido();
            if (nivelCPU != NivelEnvido.NINGUNO) {
                switch (nivelCPU) {
                    case ENVIDO -> envido.cantarEnvido();
                    case REAL_ENVIDO -> envido.subirRealEnvido();
                    case FALTA_ENVIDO -> envido.subirFaltaEnvido();
                }

                panelMensajes.agregarMensaje("🤖 CPU cantó " + nivelCPU);

                int opcion = JOptionPane.showOptionDialog(null,
                        "CPU cantó " + nivelCPU + ". ¿Qué querés hacer?",
                        "Respuesta al Envido",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"No quiero", "Quiero", "Envido", "Real Envido", "Falta Envido"},
                        "Quiero");

                if (opcion == 0) {
                    envido.rechazar();
                    jugadorCPU.sumarPuntos(envido.puntosSiNoSeAcepta());
                    panelMensajes.agregarMensaje("🙅‍♂️ No quisiste. CPU gana 1 punto.");
                    actualizarPuntuacion();
                    return;
                } else if (opcion == 1) {
                    envido.aceptar();
                } else if (opcion == 2) {
                    envido.subirEnvido();
                } else if (opcion == 3) {
                    envido.subirRealEnvido();
                } else if (opcion == 4) {
                    envido.subirFaltaEnvido();
                }

                if (envido.estaAceptado()) {
                    int eHumano = envido.calcularEnvido(jugadorHumano.getCartas());
                    int eCPU = envido.calcularEnvido(jugadorCPU.getCartas());

                    panelMensajes.agregarMensaje("📊 Tenés " + eHumano + " de envido.");
                    panelMensajes.agregarMensaje("🤖 CPU tiene " + eCPU + " de envido.");

                    Jugador ganador = (eHumano > eCPU || (eHumano == eCPU && jugadorHumano.esMano()))
                            ? jugadorHumano : jugadorCPU;

                    int puntos = envido.puntosGanados(ganador, (ganador == jugadorHumano) ? jugadorCPU : jugadorHumano);
                    ganador.sumarPuntos(puntos);
                    panelMensajes.agregarMensaje("🏆 " + ganador.getNombre() + " gana el envido por " + puntos + " puntos.");
                }

                envido.reiniciar();
                actualizarPuntuacion();

                if (ronda.rondaCompleta()) {
                    finalizarRonda();
                    return;
                }
            }
        }

        if (jugadorCPU.getCartas().isEmpty()) return;

        // RESPONDER TRUCO YA CANTADO POR CPU
        if (truco.fueCantadoPor(jugadorCPU) && truco.estaEnCurso() && !truco.estaAceptado() && !truco.estaRechazado()) {
            if (!ronda.getJugadorActual().equals(jugadorHumano)) return;

            int opcion = JOptionPane.showOptionDialog(null,
                    "CPU cantó Truco. ¿Qué querés hacer?",
                    "Respuesta al Truco",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"No quiero", "Quiero", "Retruco"},
                    "Quiero");

            if (opcion == JOptionPane.CLOSED_OPTION) {
                panelMensajes.agregarMensaje("❗ Cerraste el cartel. El Truco queda pendiente.");
                return;
            }

            if (opcion == 0) {
                truco.rechazar();
                jugadorCPU.sumarPuntos(truco.puntosSiNoSeAcepta());
                panelMensajes.agregarMensaje("🙅‍♂️ No quisiste. CPU gana " + truco.puntosSiNoSeAcepta() + " punto(s).");
                finalizarRonda();
                return;
            } else if (opcion == 1) {
                truco.aceptar();
                panelMensajes.agregarMensaje("✅ Aceptaste el Truco.");
                actualizarPuntuacion();
                actualizarVista();

                SwingUtilities.invokeLater(this::jugarTurnoCPU); // 👈 HACE QUE CPU SIGA SU TURNO
                return;
            } else if (opcion == 2) {
                truco.subirTruco();
                panelMensajes.agregarMensaje("🔥 Subiste a Retruco.");

                if (jugadorCPU.deseaAceptarRetruco()) {
                    truco.aceptar();
                    panelMensajes.agregarMensaje("🤖 CPU aceptó el Retruco.");
                    actualizarPuntuacion();
                } else {
                    truco.rechazar();
                    jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
                    panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
                    finalizarRonda();
                }
                return;
            }
        }

        // CANTAR TRUCO POR PRIMERA VEZ
        if (!truco.estaEnCurso() && jugadorCPU.deseaCantarTruco()) {
            truco.cantarTruco(jugadorCPU);
            panelMensajes.agregarMensaje("🤖 CPU cantó Truco.");

            int opcion = JOptionPane.showOptionDialog(null,
                    "CPU cantó Truco. ¿Qué querés hacer?",
                    "Respuesta al Truco",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"No quiero", "Quiero", "Retruco"},
                    "Quiero");

            if (opcion == JOptionPane.CLOSED_OPTION) {
                panelMensajes.agregarMensaje("❗ Cerraste el cartel. El Truco queda pendiente.");
                return;
            }

            if (opcion == 0) {
                truco.rechazar();
                jugadorCPU.sumarPuntos(truco.puntosSiNoSeAcepta());
                panelMensajes.agregarMensaje("🙅‍♂️ No quisiste. CPU gana " + truco.puntosSiNoSeAcepta() + " punto(s).");
                finalizarRonda();
                return;
            } else if (opcion == 1) {
                truco.aceptar();
                panelMensajes.agregarMensaje("✅ Aceptaste el Truco.");
                actualizarPuntuacion();
                actualizarVista();

                SwingUtilities.invokeLater(this::jugarTurnoCPU); // 👈 HACE QUE CPU SIGA SU TURNO
                return;
            } else if (opcion == 2) {
                truco.subirTruco();
                panelMensajes.agregarMensaje("🔥 Subiste a Retruco.");

                if (jugadorCPU.deseaAceptarRetruco()) {
                    truco.aceptar();
                    panelMensajes.agregarMensaje("🤖 CPU aceptó el Retruco.");
                    actualizarPuntuacion();
                } else {
                    truco.rechazar();
                    jugadorHumano.sumarPuntos(truco.puntosSiNoSeAcepta());
                    panelMensajes.agregarMensaje("🤖 CPU no quiso. Ganás " + truco.puntosSiNoSeAcepta() + " punto(s).");
                    finalizarRonda();
                }
                return;
            }
        }

        // CARTA DE LA CPU
        if (!ronda.rondaCompleta() && (!truco.estaEnCurso() || truco.estaAceptado())) {
            Carta carta = jugadorCPU.jugarCarta();
            if (carta == null) return;

            panelMensajes.agregarMensaje("🤖 CPU jugó: " + carta);
            ronda.jugarTurnoConCarta(jugadorCPU, carta);
            actualizarVista();
            verificarEstadoRonda();

            if (!ronda.rondaCompleta()
                    && ronda.getJugadorActual() instanceof JugadorCPU
                    && !jugadorCPU.getCartas().isEmpty()) {
                SwingUtilities.invokeLater(this::jugarTurnoCPU);
            }
        }
    }


    private void verificarEstadoRonda() {
        if (ronda.rondaCompleta()) {
            Jugador ganador = ronda.determinarGanador();
            panelMensajes.agregarMensaje("🎉 " + ganador.getNombre() + " gana la ronda.");

            int puntosGanados = truco.estaEnCurso() && !truco.estaRechazado()
                    ? truco.puntosSiSeAcepta()
                    : 1;


            ganador.sumarPuntos(puntosGanados);
            panelMensajes.agregarMensaje("🏆 Gana " + puntosGanados + " punto(s).");
            finalizarRonda();
            return;
        }

        if (ronda.getJugadorActual() instanceof JugadorCPU) {
            SwingUtilities.invokeLater(this::jugarTurnoCPU);
        }
    }

    private void irseAlMazo() {
        panelMensajes.agregarMensaje("😓 Te fuiste al mazo. CPU gana 1 punto.");
        jugadorCPU.sumarPuntos(1);
        finalizarRonda();
    }

    private void finalizarRonda() {
        actualizarPuntuacion();
        panelMensajes.agregarMensaje("🔁 Repartiendo la próxima mano...");

        // 🚫 Deshabilitar botones de acción del jugador
        panelAcciones.habilitarAcciones(false);

        // Opcional: también podrías mostrar cartas "sin carta" si querés limpiar la vista
        panelCartas.mostrarCartas(new String[]{"", "", ""});

        truco.reiniciar();

        new javax.swing.Timer(5000, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            iniciarNuevaRonda();
        }).start();
    }


}
