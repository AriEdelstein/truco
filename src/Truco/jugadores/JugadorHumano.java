package Truco.jugadores;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;

import java.util.Scanner;

public class JugadorHumano extends Jugador implements Cantante {

    private final Scanner scanner = new Scanner(System.in);

    public JugadorHumano(String nombre) {
        super(nombre);
    }

    @Override
    public Carta jugarCarta() {
        System.out.println("\n" + nombre + ", elegí una carta para jugar:");
        mostrarCartas();

        int eleccion = -1;
        while (eleccion < 1 || eleccion > cartas.size()) {
            System.out.print("Ingresá el número de la carta: ");
            try {
                eleccion = Integer.parseInt(scanner.nextLine().trim());
                if (eleccion < 1 || eleccion > cartas.size()) {
                    System.out.println("⚠️ Número fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Entrada inválida. Intentá de nuevo.");
            }
        }

        Carta cartaElegida = cartas.get(eleccion - 1);
        cartas.remove(cartaElegida);
        return cartaElegida;
    }

    @Override
    public NivelEnvido deseaCantarEnvido() {
        System.out.println("\n" + nombre + ", ¿Querés cantar Envido?");
        System.out.println("1. No cantar");
        System.out.println("2. Envido");
        System.out.println("3. Real Envido");
        System.out.println("4. Falta Envido");
        System.out.print("Elegí una opción (1-4): ");

        String opcion = scanner.nextLine().trim();
        return switch (opcion) {
            case "2" -> NivelEnvido.ENVIDO;
            case "3" -> NivelEnvido.REAL_ENVIDO;
            case "4" -> NivelEnvido.FALTA_ENVIDO;
            default -> NivelEnvido.NINGUNO;
        };
    }

    @Override
    public boolean deseaCantarTruco() {
        System.out.print(nombre + ", ¿Querés cantar Truco? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    @Override
    public boolean deseaAceptarRetruco() {
        System.out.print(nombre + ", ¿Aceptás el Retruco? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    @Override
    public boolean deseaAceptarValeCuatro() {
        System.out.print(nombre + ", ¿Aceptás el Vale Cuatro? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    public Carta jugarCartaDesdeGUI(int indice) {
        return cartas.remove(indice);
    }

}
