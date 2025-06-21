package Truco.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PanelCartasJugador extends JPanel {

    private ArrayList<JButton> botonesCartas;

    public PanelCartasJugador() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        setBorder(BorderFactory.createTitledBorder("Tus cartas"));

        botonesCartas = new ArrayList<>();

        // Inicialmente creamos 3 botones vacíos (luego se actualizarán)
        for (int i = 0; i < 3; i++) {
            JButton btnCarta = new JButton("Carta " + (i + 1));
            btnCarta.setEnabled(false); // Se habilitan cuando hay cartas reales
            botonesCartas.add(btnCarta);
            add(btnCarta);
        }
    }

    // Método para actualizar el texto de las cartas mostradas
    public void mostrarCartas(String[] nombresCartas) {
        for (int i = 0; i < botonesCartas.size(); i++) {
            if (i < nombresCartas.length) {
                botonesCartas.get(i).setText(nombresCartas[i]);
                botonesCartas.get(i).setEnabled(true);
            } else {
                botonesCartas.get(i).setText("Sin carta");
                botonesCartas.get(i).setEnabled(false);
            }
        }
    }

    // Asignar una acción a cada carta por índice
    public void setAccionCarta(int indice, ActionListener listener) {
        if (indice >= 0 && indice < botonesCartas.size()) {
            JButton btn = botonesCartas.get(indice);

            // Limpia listeners anteriores
            for (ActionListener al : btn.getActionListeners()) {
                btn.removeActionListener(al);
            }

            btn.addActionListener(listener);
        }
    }

    // Deshabilitar todas las cartas (por turno o si ya se jugó)
    public void deshabilitarCartas() {
        for (JButton btn : botonesCartas) {
            btn.setEnabled(false);
        }
    }
}
