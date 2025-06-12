package Truco.cantos;

import Truco.cartas.Carta;
import Truco.jugadores.Jugador;

import java.util.List;

public class Envido {
    private int puntosEnJuego;
    private boolean enCurso;
    private boolean faltaEnvido;
    private boolean aceptado;
    private boolean rechazado;

    public Envido() {
        reiniciar();
    }

    public void cantarEnvido() {
        if (!enCurso) {
            puntosEnJuego = 2;
            enCurso = true;
            //System.out.println("🎙️ Se canta ENVIDO.");
        } else {
            subirEnvido(); // Por si se intenta repetir
        }
    }

    public void subirEnvido() {
        if (faltaEnvido || puntosEnJuego >= 3) {
            System.out.println("⚠️ No se puede subir a Envido desde Real o Falta Envido.");
            return;
        }
        if (puntosEnJuego == 2) {
            puntosEnJuego = 4;
            System.out.println("🎙️ Se sube a ENVIDO-ENVIDO.");
        } else if (puntosEnJuego == 4) {
            puntosEnJuego = 7;
            System.out.println("🎙️ Se sube a ENVIDO-ENVIDO-REAL ENVIDO.");
        } else {
            System.out.println("⚠️ No se puede subir más con esta cadena.");
        }
    }

    public void subirRealEnvido() {
        if (faltaEnvido || puntosEnJuego >= 7) {
            System.out.println("⚠️ No se puede subir a Real Envido en este contexto.");
            return;
        }
        puntosEnJuego = 3;
        enCurso = true;
        //System.out.println("🎙️ Se canta REAL ENVIDO.");
    }

    public void subirFaltaEnvido() {
        if (faltaEnvido) {
            System.out.println("⚠️ Ya se cantó Falta Envido.");
            return;
        }
        puntosEnJuego = -1; // Falta envido
        faltaEnvido = true;
        enCurso = true;
        //System.out.println("🎙️ Se canta FALTA ENVIDO.");
    }

    public void aceptar() {
        aceptado = true;
        System.out.println("✅ Envido aceptado.");
    }

    public void rechazar() {
        rechazado = true;
        System.out.println("❌ Envido no aceptado.");
    }

    public int puntosGanados(Jugador ganador, Jugador perdedor) {
        if (!aceptado) return 1;

        if (faltaEnvido) {
            if (ganador.getPuntos() < 15 && perdedor.getPuntos() < 15) {
                return 15 - perdedor.getPuntos();
            } else {
                return 30 - perdedor.getPuntos();
            }
        }

        return puntosEnJuego;
    }

    public int puntosSiNoSeAcepta() {
        return 1;
    }

    public int calcularEnvido(List<Carta> cartas) {
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
    public boolean esFaltaEnvido() {
        return faltaEnvido;
    }

    public boolean esRealEnvido() {
        return puntosEnJuego == 3;
    }

    public boolean estaEnCurso() {
        return enCurso;
    }

    public boolean estaAceptado() {
        return aceptado;
    }

    public boolean estaRechazado() {
        return rechazado;
    }

    public int getNivelEnvido() {
        return faltaEnvido ? 99 : puntosEnJuego;
    }

    public void reiniciar() {
        puntosEnJuego = 0;
        enCurso = false;
        faltaEnvido = false;
        aceptado = false;
        rechazado = false;
    }
}
