package Truco.util;

import Truco.cartas.Carta;

import java.util.List;

public class Utils {

    /**
     * Devuelve el valor de Truco de una carta.
     * Menor número = mayor jerarquía.
     */
    public static int valorTruco(Carta carta) {
        return carta.getValorTruco();
    }

    /**
     * Calcula el mejor envido posible de una mano.
     * Se basa en combinación de palos o mayor valor individual.
     */
    public static int calcularEnvido(List<Carta> cartas) {
        int max = 0;

        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                Carta c1 = cartas.get(i);
                Carta c2 = cartas.get(j);
                if (c1.getPalo() == c2.getPalo()) {
                    int valor = c1.getValorEnvido() + c2.getValorEnvido() + 20;
                    max = Math.max(max, valor);
                } else {
                    max = Math.max(max, c1.getValorEnvido());
                    max = Math.max(max, c2.getValorEnvido());
                }
            }
        }

        return max;
    }

    /**
     * Compara dos cartas según jerarquía del Truco.
     * @return 1 si c1 gana, -1 si c2 gana, 0 si empate
     */
    public static int compararCartasTruco(Carta c1, Carta c2) {
        int v1 = valorTruco(c1);
        int v2 = valorTruco(c2);

        return Integer.compare(v2, v1); // menor gana
    }
}
