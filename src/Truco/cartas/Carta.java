package Truco.cartas;

public class Carta {
    private final int numero;         // 1 al 7, 10, 11, 12
    private final Palo palo;

    public Carta(int numero, Palo palo) {
        this.numero = numero;
        this.palo = palo;
    }

    public Palo getPalo() {
        return palo;
    }

    // Valor para jerarquía del Truco (menor valor = mayor jerarquía)
    public int getValorTruco() {
        // Ranking personalizado del Truco
        if (numero == 1 && palo == Palo.ESPADA) return 1;
        if (numero == 1 && palo == Palo.BASTO) return 2;
        if (numero == 7 && palo == Palo.ESPADA) return 3;
        if (numero == 7 && palo == Palo.ORO) return 4;
        if (numero == 3) return 5;
        if (numero == 2) return 6;
        if (numero == 1) return 7;
        if (numero == 12) return 8;
        if (numero == 11) return 9;
        if (numero == 10) return 10;
        if (numero == 7) return 11;
        if (numero == 6) return 12;
        if (numero == 5) return 13;
        return 14; // el 4 es el más bajo
    }

    // Valor para envido (máximo hasta 7, sino vale 0)
    public int getValorEnvido() {
        return (numero >= 10) ? 0 : numero;
    }

    @Override
    public String toString() {
        return numero + " de " + palo;
    }
}
