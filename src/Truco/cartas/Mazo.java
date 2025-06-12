package Truco.cartas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mazo {
    private final List<Carta> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        inicializarMazo();
    }

    private void inicializarMazo() {
        for (Palo palo : Palo.values()) {
            for (int numero = 1; numero <= 12; numero++) {
                if (numero != 8 && numero != 9) {
                    cartas.add(new Carta(numero, palo));
                }
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public List<Carta> repartirCartas(int cantidad) {
        List<Carta> mano = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            if (!cartas.isEmpty()) {
                mano.add(cartas.remove(0));
            }
        }
        return mano;
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public void reiniciar() {
        cartas.clear();
        inicializarMazo();
        barajar();
    }
}
