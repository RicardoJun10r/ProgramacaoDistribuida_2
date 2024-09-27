package client.grupoAberto;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class UsuarioAberto {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        EmissorUsuario emissor = new EmissorUsuario(55554, "225.7.8.9", "USUARIO");

        Runnable cliente = () -> {
            System.out.println("Iniciando comunicação...");
            emissor.init();
        };

        scheduler.schedule(cliente, 0, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            scheduler.shutdown();
            System.out.println("Encerrando o serviço de " +
                    "monitoramento de elementos climáticos.");
        }, 30, TimeUnit.SECONDS);
    }
}
