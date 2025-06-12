package Truco.jugadores;

import Truco.cantos.NivelEnvido;
import Truco.cartas.Carta;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class JugadorCPU extends Jugador implements Cantante {

    private final Random random = new Random();

    public JugadorCPU(String nombre) {
        super(nombre);
    }

    @Override
    public Carta jugarCarta() {
        Carta cartaElegida = cartas.stream()
                .min(Comparator.comparingInt(Carta::getValorTruco))
                .orElse(null);

        cartas.remove(cartaElegida);
        return cartaElegida;
    }

    @Override
    public NivelEnvido deseaCantarEnvido() {
        int envido = calcularEnvido();

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

        return NivelEnvido.NINGUNO;
    }

    public boolean deseaAceptarEnvido(int nivel, int envidoCantadoPorRival) {
        int envido = calcularEnvido();

        return switch (nivel) {
            case 1 -> envido >= 21;
            case 2 -> envido >= 27;
            case 3 -> envido >= 31;
            default -> false;
        };
    }

    private int calcularEnvido() {
        int maxEnvido = 0;

        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                Carta c1 = cartas.get(i);
                Carta c2 = cartas.get(j);
                if (c1.getPalo() == c2.getPalo()) {
                    int total = c1.getValorEnvido() + c2.getValorEnvido() + 20;
                    maxEnvido = Math.max(maxEnvido, total);
                } else {
                    maxEnvido = Math.max(maxEnvido, c1.getValorEnvido());
                    maxEnvido = Math.max(maxEnvido, c2.getValorEnvido());
                }
            }
        }

        return maxEnvido;
    }

    @Override
    public boolean deseaCantarTruco() {
        boolean tieneFuerte = cartas.stream().anyMatch(carta -> carta.getValorTruco() <= 4);
        return tieneFuerte || random.nextInt(100) < 15;
    }

    @Override
    public boolean deseaAceptarTruco() {
        return cartas.stream().anyMatch(c -> c.getValorTruco() <= 6) || random.nextBoolean();
    }

    @Override
    public boolean deseaCantarRetruco() {
        return calcularFuerzaCartas() >= 2 || random.nextInt(100) < 20;
    }

    @Override
    public boolean deseaCantarValeCuatro() {
        return calcularFuerzaCartas() >= 3 || random.nextInt(100) < 10;
    }

    @Override
    public boolean deseaAceptarRetruco() {
        return calcularFuerzaCartas() >= 2 || random.nextBoolean();
    }

    @Override
    public boolean deseaAceptarValeCuatro() {
        return calcularFuerzaCartas() >= 3;
    }

    public boolean deseaAceptarRetrucoYSubir(Carta cartaJugada) {
        boolean acepta = cartaJugada.getValorTruco() <= 6 || random.nextBoolean();

        if (!acepta) {
            return false;
        }

        boolean sube = cartaJugada.getValorTruco() <= 3 || random.nextInt(100) < 20;
        if (sube) {
            System.out.println(nombre + " sube a VALE CUATRO.");
        }

        return true;
    }

    private int calcularFuerzaCartas() {
        return (int) cartas.stream()
                .filter(c -> c.getValorTruco() <= 7)
                .count();
    }
}
