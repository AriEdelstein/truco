package Truco.jugadores;

import Truco.cartas.Carta;
import java.util.ArrayList;
import java.util.List;

public abstract class Jugador {
    protected String nombre;
    protected List<Carta> cartas;
    protected int puntos;
    protected boolean esMano;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.cartas = new ArrayList<>();
        this.puntos = 0;
        this.esMano = false;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Carta> getCartas() {
        return cartas;
    }

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void setCartas(List<Carta> cartas) {
        this.cartas = cartas;
    }

    public void setEsMano(boolean esMano) {
        this.esMano = esMano;
    }

    public boolean esMano() {
        return esMano;
    }

    public abstract Carta jugarCarta();

    public void mostrarCartas() {
        for (int i = 0; i < cartas.size(); i++) {
            System.out.println((i + 1) + ". " + cartas.get(i));
        }
    }
}
