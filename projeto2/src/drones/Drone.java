package drones;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import server.grupoAberto.Emissor;

import java.io.IOException;
import java.util.Arrays;

public class Drone {
    public static void main(String[] args) throws IOException {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        Emissor emissor = new Emissor(55554, "225.7.8.9", "DRONE");

        Runnable dados = () -> {
            double[] elementos = coletarElementos();
            System.out.println("Elementos climáticos atual: " + Arrays.toString(elementos));
            emissor.addQueue(elementos);
        };

        Runnable cliente = () -> {
            System.out.println("Iniciando comunicação...");
            emissor.init();
        };

        scheduler.schedule(cliente, 0, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(dados, 1, 3, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            scheduler.shutdown();
            System.out.println("Encerrando o serviço de " +
                    "monitoramento de elementos climáticos.");
        }, 30, TimeUnit.SECONDS);
    }

    private static double[] coletarElementos() {
        double[] elementos = new double[4];
        for (int i = 0; i < 4; i++)
            elementos[i] = Math.random() * 10;
        return elementos;
    }
}
