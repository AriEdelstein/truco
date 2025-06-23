package Truco.jugadores;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;

import java.util.Scanner;

public class JugadorHumano extends Jugador implements Cantante {

    // Scanner para entrada por consola
    private final Scanner scanner = new Scanner(System.in);

    // Constructor: inicializa con nombre
    public JugadorHumano(String nombre) {
        super(nombre);
    }

    // Metodo que permite al jugador humano elegir una carta desde consola
    @Override
    public Carta jugarCarta() {
        System.out.println("\n" + nombre + ", elegí una carta para jugar:");
        mostrarCartas(); // Muestra la mano con numeración

        int eleccion = -1;
        // Solicita una opción válida
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

        // Selecciona la carta y la elimina de la mano
        Carta cartaElegida = cartas.get(eleccion - 1);
        cartas.remove(cartaElegida);
        return cartaElegida;
    }

    // Le pregunta al usuario si quiere cantar Envido y cuál
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

    // Pregunta si desea cantar Truco
    @Override
    public boolean deseaCantarTruco() {
        System.out.print(nombre + ", ¿Querés cantar Truco? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    // Pregunta si acepta Retruco
    @Override
    public boolean deseaAceptarRetruco() {
        System.out.print(nombre + ", ¿Aceptás el Retruco? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    // Pregunta si acepta Vale Cuatro
    @Override
    public boolean deseaAceptarValeCuatro() {
        System.out.print(nombre + ", ¿Aceptás el Vale Cuatro? (s/n): ");
        return scanner.nextLine().trim().equalsIgnoreCase("s");
    }

    // Metodo auxiliar para jugadas desde interfaz gráfica (GUI)
    public Carta jugarCartaDesdeGUI(int indice) {
        return cartas.remove(indice); // Elimina y devuelve la carta elegida por índice
    }
}
