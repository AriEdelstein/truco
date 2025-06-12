package Truco;
import Truco.consola.JuegoConsola;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("🃏 Bienvenido al Truco Argentino");
        System.out.println("¿Cómo querés jugar?");
        System.out.println("1. Consola");
        System.out.println("2. Gráfico (no implementado aún)");

        int opcion = -1;
        while (opcion != 1) {
            System.out.print("Elegí una opción (1): ");
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
            System.out.println("🚧 La versión gráfica está en desarrollo.");
        }

        scanner.close();
    }
}
