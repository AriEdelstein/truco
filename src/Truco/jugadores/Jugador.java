package Truco.jugadores;

import Truco.cartas.Carta;
import java.util.ArrayList;
import java.util.List;

public abstract class Jugador {
    protected String nombre;            // Nombre del jugador
    protected List<Carta> cartas;       // Cartas en mano del jugador
    protected int puntos;               // Puntos acumulados
    protected boolean esMano;           // Indica si es mano en la ronda actual

    // Constructor: inicializa nombre, lista de cartas vacía y puntos en 0
    public Jugador(String nombre) {
        this.nombre = nombre;
        this.cartas = new ArrayList<>();
        this.puntos = 0;
        this.esMano = false;
    }

    // Devuelve el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    // Devuelve la lista de cartas en mano
    public List<Carta> getCartas() {
        return cartas;
    }

    // Devuelve los puntos actuales del jugador
    public int getPuntos() {
        return puntos;
    }

    // Suma puntos al total del jugador
    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    // Asigna las cartas de la mano actual
    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    // Define si este jugador es "mano" (el que empieza la ronda)
    public void setEsMano(boolean esMano) {
        this.esMano = esMano;
    }

    // Devuelve si este jugador es el mano
    public boolean esMano() {
        return esMano;
    }

    // Metodo abstracto que debe implementar cada tipo de jugador (humano o CPU)
    public abstract Carta jugarCarta();

    // Muestra las cartas actuales en la consola (usado para humanos)
    public void mostrarCartas() {
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println((i + 1) + ". " + cartas.get(i));
        }
    }
}
