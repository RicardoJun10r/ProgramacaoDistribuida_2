package client.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmissorUsuario {
    private final int PORTA;
    private final String GRUPO;
    private final String NICKNAME;

    private DatagramSocket socket;

    public EmissorUsuario(int porta, String grupo, String nickname) {
        this.PORTA = porta;
        this.GRUPO = grupo;
        this.NICKNAME = nickname;
    }

    public void init() {
        try {
            this.socket = new DatagramSocket();
            System.out.println("Processo [Emissor] executando: [" + InetAddress.getLocalHost() + ":"
                    + this.socket.getLocalPort() + "]");

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            Runnable enviarRequisicao = () -> {
                try {
                    byte[] buffer_envio = (this.NICKNAME + ";" + InetAddress.getLocalHost() + ":" + this.socket.getLocalPort()).getBytes();
                    DatagramPacket packet = new DatagramPacket(buffer_envio, buffer_envio.length, InetAddress.getByName(this.GRUPO), this.PORTA);
                    this.socket.send(packet);
                    System.out.println("Requisição enviada para o grupo.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };

            scheduler.scheduleAtFixedRate(enviarRequisicao, 0, 5, TimeUnit.SECONDS);

            byte[] buffer_recepcao = new byte[1024];
            while (true) {
                DatagramPacket packetRecepcao = new DatagramPacket(buffer_recepcao, buffer_recepcao.length);
                this.socket.receive(packetRecepcao);
                String line = new String(packetRecepcao.getData(), 0, packetRecepcao.getLength());
                System.out.println("Resposta recebida: " + line);

                if (line.contains("sair")) {
                    System.out.println("Finalizando comunicação.");
                    scheduler.shutdown();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }
        }
    }
}
