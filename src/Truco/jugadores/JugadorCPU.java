package Truco.jugadores;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;

import java.util.Comparator;
import java.util.Random;

public class JugadorCPU extends Jugador implements Cantante {

    private final Random random = new Random(); // Para generar decisiones aleatorias

    public JugadorCPU(String nombre) {
        super(nombre); // Llama al constructor de Jugador
    }

    @Override
    public Carta jugarCarta() {
        if (cartas.isEmpty()) {
            System.err.println("⚠️ CPU intentó jugar sin cartas.");
            return null;
        }

        // Elige la carta más débil según la jerarquía del Truco (menor valorTruco)
        Carta cartaElegida = cartas.stream()
                .min(Comparator.comparingInt(Carta::getValorTruco))
                .orElseThrow(); // No debería lanzar nada porque ya chequeamos que no está vacía

        cartas.remove(cartaElegida); // Elimina la carta jugada de la mano
        return cartaElegida;         // Devuelve la carta jugada
    }

    // Decide si cantar Envido y cuál tipo, según su valor de envido
    @Override
    public NivelEnvido deseaCantarEnvido() {
        int envido = calcularEnvido(); // Calcula el envido con las cartas en mano

        if (envido >= 33) {
            System.out.println(nombre + " canta Falta Envido.");
            return NivelEnvido.FALTA_ENVIDO;
        } else if (envido >= 30) {
            System.out.println(nombre + " canta Real Envido.");
            return NivelEnvido.REAL_ENVIDO;
        } else if (envido >= 25) {
            System.out.println(nombre + " canta Envido.");
            return NivelEnvido.ENVIDO;
        }

        return NivelEnvido.NINGUNO; // No canta si el valor es bajo
    }

    // Decide si aceptar el Envido según su propio puntaje y el nivel del canto rival
    public boolean deseaAceptarEnvido(int nivel, int envidoCantadoPorRival) {
        int envido = calcularEnvido();

        // Umbrales por nivel del envido
        return switch (nivel) {
            case 1 -> envido >= 21;  // Envido
            case 2 -> envido >= 27;  // Real Envido
            case 3 -> envido >= 31;  // Falta Envido
            default -> false;
        };
    }

    // Calcula el mejor valor de envido que puede formar con sus cartas
    private int calcularEnvido() {
        int maxEnvido = 0;

        // Recorre todas las combinaciones posibles de 2 cartas
        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                Carta c1 = cartas.get(i);
                Carta c2 = cartas.get(j);

                if (c1.getPalo() == c2.getPalo()) {
                    // Si son del mismo palo, suman +20
                    int total = c1.getValorEnvido() + c2.getValorEnvido() + 20;
                    maxEnvido = Math.max(maxEnvido, total);
                } else {
                    // Si son de distinto palo, toma el valor más alto individual
                    maxEnvido = Math.max(maxEnvido, c1.getValorEnvido());
                    maxEnvido = Math.max(maxEnvido, c2.getValorEnvido());
                }
            }
        }

        return maxEnvido;
    }

    // === Respuestas al Truco y variantes ===

    @Override
    public boolean deseaAceptarTruco() {
        // Acepta Truco si tiene al menos una carta con valor fuerte (≤ 10)
        return cartas.stream().anyMatch(c -> c.getValorTruco() <= 10);
    }

    @Override
    public boolean deseaAceptarRetruco() {
        // Acepta Retruco si tiene alguna carta ≤ 6
        return cartas.stream().anyMatch(c -> c.getValorTruco() <= 6);
    }

    @Override
    public boolean deseaAceptarValeCuatro() {
        // Acepta Vale Cuatro si tiene alguna carta ≤ 3
        return cartas.stream().anyMatch(c -> c.getValorTruco() <= 3);
    }

    // Decide si cantar Truco
    @Override
    public boolean deseaCantarTruco() {
        // Si tiene carta muy fuerte (≤ 4) o con 10% de probabilidad
        boolean tieneCartaFuerte = cartas.stream().anyMatch(c -> c.getValorTruco() <= 4);
        return tieneCartaFuerte || random.nextInt(100) < 10;
    }

    // Decide si subir a Vale Cuatro
    @Override
    public boolean deseaCantarValeCuatro() {
        // Si tiene al menos 3 cartas fuertes (≤ 7) o con 10% de probabilidad
        return calcularFuerzaCartas() >= 3 || random.nextInt(100) < 10;
    }

    // Cuenta cuántas cartas fuertes (≤ 7) tiene
    private int calcularFuerzaCartas() {
        return (int) cartas.stream()
                .filter(c -> c.getValorTruco() <= 7)
                .count();
    }
}
