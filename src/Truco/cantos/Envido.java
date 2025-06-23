package Truco.cantos;

import Truco.cartas.Carta;
import Truco.jugadores.Jugador;

import java.util.List;

public class Envido {
    // Atributos que indican el estado del canto Envido
    private int puntosEnJuego;       // Puntos que se están jugando en el Envido
    private boolean enCurso;         // Indica si hay un Envido activo
    private boolean faltaEnvido;     // Indica si se cantó Falta Envido
    private boolean aceptado;        // Indica si el Envido fue aceptado
    private boolean rechazado;       // Indica si el Envido fue rechazado

    public Envido() {
        reiniciar(); // Inicializa todos los valores al comenzar
    }

    // Método para cantar Envido por primera vez
    public void cantarEnvido() {
        if (!enCurso) {
            puntosEnJuego = 2;      // Envido vale 2 puntos inicialmente
            enCurso = true;         // Se marca como en curso
        } else {
            subirEnvido();          // Si ya está en curso, intenta subir el canto
        }
    }

    // Método para subir el Envido (Envido → Envido-Envido → Real Envido)
    public void subirEnvido() {
        if (faltaEnvido || puntosEnJuego >= 3) {
            // No se puede subir si ya hay un Real o Falta Envido
            System.out.println("⚠️ No se puede subir a Envido desde Real o Falta Envido.");
            return;
        }
        if (puntosEnJuego == 2) {
            puntosEnJuego = 4; // Envido → Envido-Envido
            System.out.println("🎙️ Se sube a ENVIDO-ENVIDO.");
        } else if (puntosEnJuego == 4) {
            puntosEnJuego = 7; // Envido-Envido → Envido-Envido-Real Envido
            System.out.println("🎙️ Se sube a ENVIDO-ENVIDO-REAL ENVIDO.");
        } else {
            // No se puede subir más
            System.out.println("⚠️ No se puede subir más con esta cadena.");
        }
    }

    // Método para cantar Real Envido directamente
    public void subirRealEnvido() {
        if (faltaEnvido || puntosEnJuego >= 7) {
            // No se puede cantar Real si ya hay Falta o una cadena más alta
            System.out.println("⚠️ No se puede subir a Real Envido en este contexto.");
            return;
        }
        puntosEnJuego = 3; // Real Envido vale 3 puntos
        enCurso = true;
    }

    // Método para cantar Falta Envido
    public void subirFaltaEnvido() {
        if (faltaEnvido) {
            // No se puede cantar Falta dos veces
            System.out.println("⚠️ Ya se cantó Falta Envido.");
            return;
        }
        puntosEnJuego = -1; // Se usa -1 como marcador especial
        faltaEnvido = true;
        enCurso = true;
    }

    // El jugador acepta el Envido
    public void aceptar() {
        aceptado = true;
        System.out.println("✅ Envido aceptado.");
    }

    // El jugador rechaza el Envido
    public void rechazar() {
        rechazado = true;
        System.out.println("❌ Envido no aceptado.");
    }

    // Calcula los puntos ganados según el tipo de Envido y los puntos de los jugadores
    public int puntosGanados(Jugador ganador, Jugador perdedor) {
        if (!aceptado) return 1; // Si no se acepta, solo se gana 1 punto

        if (faltaEnvido) {
            // Falta Envido: se gana lo necesario para que el perdedor llegue a 15 o 30
            if (ganador.getPuntos() < 15 && perdedor.getPuntos() < 15) {
                return 15 - perdedor.getPuntos();
            } else {
                return 30 - perdedor.getPuntos();
            }
        }

        return puntosEnJuego; // Para Envido, Envido-Envido o Real Envido
    }

    // Si se rechaza el Envido, el oponente gana 1 punto
    public int puntosSiNoSeAcepta() {
        return 1;
    }

    // Calcula el valor del Envido con una lista de 3 cartas
    public int calcularEnvido(List<Carta> cartas) {
        int maxEnvido = 0;
        // Se prueban todas las combinaciones de dos cartas
        for (int i = 0; i < cartas.size(); i++) {
            for (int j = i + 1; j < cartas.size(); j++) {
                Carta c1 = cartas.get(i);
                Carta c2 = cartas.get(j);
                if (c1.getPalo() == c2.getPalo()) {
                    // Si son del mismo palo: suma de valores + 20
                    int total = c1.getValorEnvido() + c2.getValorEnvido() + 20;
                    maxEnvido = Math.max(maxEnvido, total);
                } else {
                    // Si no son del mismo palo: se toma el mayor individual
                    maxEnvido = Math.max(maxEnvido, c1.getValorEnvido());
                    maxEnvido = Math.max(maxEnvido, c2.getValorEnvido());
                }
            }
        }
        return maxEnvido;
    }

    // Métodos de consulta (getters booleanos)
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

    // Devuelve los puntos del Envido actual, usando 99 como marcador especial de Falta Envido
    public int getNivelEnvido() {
        return faltaEnvido ? 99 : puntosEnJuego;
    }

    // Reinicia todos los estados a valores iniciales
    public void reiniciar() {
        puntosEnJuego = 0;
        enCurso = false;
        faltaEnvido = false;
        aceptado = false;
        rechazado = false;
    }
}
