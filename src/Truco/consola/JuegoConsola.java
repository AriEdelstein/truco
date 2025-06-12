package Truco.consola;

import Truco.juego.Juego;
import Truco.jugadores.JugadorHumano;
import Truco.jugadores.JugadorCPU;

import java.util.Scanner;

public class JuegoConsola {
    private final Scanner scanner = new Scanner(System.in);

    public void iniciar() {
        System.out.println("🎮 Bienvenido al Truco (modo consola)");
        System.out.print("Ingresá tu nombre: ");
        String nombreHumano = scanner.nextLine().trim();

        JugadorHumano jugadorHumano = new JugadorHumano(nombreHumano);
        JugadorCPU jugadorCpu = new JugadorCPU("CPU");

        Juego juego = new Juego(jugadorHumano, jugadorCpu);
        juego.jugar();
    }
}
