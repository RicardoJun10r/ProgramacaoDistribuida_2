package server.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Arrays;

public class Emissor {

    private final int PORTA;
    
    private final String GRUPO;

    private final String NICKNAME;
    
    private DatagramSocket socket;
    
    private BlockingQueue<double[]> fila;

    public Emissor(int porta, String grupo, String nickname) {
        this.PORTA = porta;
        this.GRUPO = grupo;
        this.NICKNAME = nickname;
        this.fila = new LinkedBlockingQueue<>();
    }

    public void addQueue(double[] elementos) {
        this.fila.add(elementos);
    }

    public void init() {
        try {
            this.socket = new DatagramSocket();
            System.out.println("Processo [Emissor] executando: [" + InetAddress.getLocalHost() + ":"
                    + this.socket.getLocalPort() + "]");

            boolean flag = true;

            while (flag) {
                double[] elementos = this.fila.take();
                byte[] buffer_envio = (this.NICKNAME + Arrays.toString(elementos)).getBytes();

                DatagramPacket packet = new DatagramPacket(
                        buffer_envio, buffer_envio.length,
                        InetAddress.getByName(this.GRUPO),
                        this.PORTA);
                this.socket.send(packet);

                if (elementos[0] == -1.0) {
                    flag = false;
                    System.out.println("Processo [Emissor] fechando: [" + InetAddress.getLocalHost() + ":"
                            + this.socket.getLocalPort() + "]");
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            this.socket.close();
        }
    }
}
