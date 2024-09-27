package drones;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.net.DatagramPacket;
import java.util.concurrent.TimeUnit;
import java.util.Random;

public class DroneFechado {

    public static void main(String[] args) {
        try {
            DroneFechado droneFechado = new DroneFechado();
            droneFechado.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private final InetAddress multicastIP;
    private final int porta;
    private final MulticastSocket socket;
    private final Random random;

    public DroneFechado() throws IOException {
        this.porta = 56789;
        this.multicastIP = InetAddress.getByName("225.7.8.9");
        this.socket = new MulticastSocket();
        this.random = new Random();
    }

    public void init() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable enviarMensagem = () -> {
            try {
                String mensagem = gerarDadosClimaticos();
                byte[] bufferEnvio = mensagem.getBytes();

                DatagramPacket pacoteEnvio = new DatagramPacket(
                        bufferEnvio, bufferEnvio.length, multicastIP, porta);

                System.out.println("Drone enviando dados climáticos: " + mensagem);
                socket.send(pacoteEnvio);

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        scheduler.scheduleAtFixedRate(enviarMensagem, 0, 5, TimeUnit.SECONDS);
    }

    private String gerarDadosClimaticos() {
        double pressaoAtmosferica = 950 + (1050 - 950) * random.nextDouble();
        double radiacaoSolar = 100 + (1000 - 100) * random.nextDouble();
        double temperatura = -10 + (40 + 10) * random.nextDouble();
        double umidade = 0 + (100 - 0) * random.nextDouble();
        return String.format("Pressão: %.2f hPa, Radiação: %.2f W/m², Temperatura: %.2f ºC, Umidade: %.2f%%", 
                             pressaoAtmosferica, radiacaoSolar, temperatura, umidade);
    }

    

}
