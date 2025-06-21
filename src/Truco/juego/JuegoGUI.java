package Truco.juego;

import Truco.jugadores.JugadorCPU;
import Truco.jugadores.JugadorHumano;

public class JuegoGUI {
    private final Juego juego;
    private Ronda ronda;

    public JuegoGUI(JugadorHumano humano, JugadorCPU cpu) {
        juego = new Juego(humano, cpu);
        nuevaRonda();
    }

    public void nuevaRonda() {
        juego.getMazo().reiniciar();
        ronda = new Ronda(juego.getJugadorMano(), juego.getJugadorPie());
        juego.getJugadorMano().setCartas(juego.getMazo().repartirCartas(3));
        juego.getJugadorPie().setCartas(juego.getMazo().repartirCartas(3));
    }

    public Ronda getRonda() {
        return ronda;
    }

    public Juego getJuego() {
        return juego;
    }
}
