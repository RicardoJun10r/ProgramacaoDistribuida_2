package drones;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Drone {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable coletaDeTemperatura = () -> {
            double temperatura = coletarTemperatura();
            System.out.println("Temperatura atual: " + temperatura + "°C");
        };

        scheduler.scheduleAtFixedRate(
                coletaDeTemperatura, 0, 3, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            scheduler.shutdown();
            System.out.println("Encerrando o serviço de " +
                    "monitoramento de temperatura.");
        }, 30, TimeUnit.SECONDS);
    }

    private static double coletarTemperatura() {
        return 10 + (Math.random() * 40);
    }
}
