package Truco.cartas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    // Lista que contiene todas las cartas del mazo
    private final List<Carta> cartas;

    // Constructor: crea e inicializa el mazo
    public Mazo() {
        cartas = new ArrayList<>();
        inicializarMazo(); // Crea las 40 cartas del mazo
    }

    // Metodo privado para llenar el mazo con las cartas del Truco
    private void inicializarMazo() {
        for (Palo palo : Palo.values()) {                // Recorre todos los palos (ESPADA, BASTO, etc.)
            for (int numero = 1; numero <= 12; numero++) {
                if (numero != 8 && numero != 9) {         // Se omiten el 8 y 9 (no se usan en Truco)
                    cartas.add(new Carta(numero, palo));  // Agrega la carta al mazo
                }
            }
        }
    }

    // Mezcla aleatoriamente las cartas del mazo
    public void barajar() {
        Collections.shuffle(cartas);
    }

    // Reparte una cantidad específica de cartas desde el tope del mazo
    public List<Carta> repartirCartas(int cantidad) {
        List<Carta> mano = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            if (!cartas.isEmpty()) {
                mano.add(cartas.remove(0)); // Saca la carta del mazo y la agrega a la mano
            }
        }
        return mano; // Devuelve la mano con las cartas repartidas
    }

    // Reinicia el mazo: limpia, vuelve a generar y baraja
    public void reiniciar() {
        cartas.clear();       // Vacía la lista de cartas
        inicializarMazo();    // Vuelve a llenar el mazo
        barajar();            // Lo baraja automáticamente
    }
}
