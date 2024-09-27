package server.grupoFechado;

import java.io.IOException;

public class ServidorMain {
    public static void main(String[] args) {
        Thread servidor1 = new Thread(() -> {
            try {
                new ServidorFechado(1).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread servidor2 = new Thread(() -> {
            try {
                new ServidorFechado(2).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread servidor3 = new Thread(() -> {
            try {
                new ServidorFechado(3).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread servidor4 = new Thread(() -> {
            try {
                new ServidorFechado(4).init();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        servidor1.start();
        servidor2.start();
        servidor3.start();
        servidor4.start();
    }
}
