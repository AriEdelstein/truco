package Truco;

import Truco.consola.JuegoConsola;
import Truco.vista.VentanaInicio;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🃏 Bienvenido al Truco Argentino");
        System.out.println("¿Cómo querés jugar?");
        System.out.println("1. Consola");
        System.out.println("2. Gráfica");

        int opcion = -1;
        while (opcion != 1 && opcion != 2) {
            System.out.print("Elegí una opción (1 o 2): ");
            try {
                opcion = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida.");
            }
        }

        if (opcion == 1) {
            JuegoConsola juegoConsola = new JuegoConsola();
            juegoConsola.iniciar();
        } else {
            SwingUtilities.invokeLater(VentanaInicio::new);
        }

        scanner.close();
    }
}
