package server.grupoFechado;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServidorFechado {

    private int id;
    
    private static BlockingQueue<String> filaMensagens = new LinkedBlockingQueue<>();

    public ServidorFechado(int id) {
        this.id = id;
    }

    public void init() throws IOException {
        if (id == 1 || id == 2) {
            System.out.println("Iniciando Servidor " + id + " (Recepção)");
            receberMensagensGrupo1();
        } else if (id == 3 || id == 4) {
            System.out.println("Iniciando Servidor " + id + " (Reenvio)");
            repassarMensagensParaGrupo2();
        }
    }

    private void receberMensagensGrupo1() throws IOException {
        int portaGrupo1 = 56789;
        MulticastSocket socketGrupo1 = new MulticastSocket(portaGrupo1);
        InetAddress multicastIPGrupo1 = InetAddress.getByName("225.7.8.9");
        socketGrupo1.joinGroup(new InetSocketAddress(multicastIPGrupo1, portaGrupo1), null);

        byte[] bufferRecepcao = new byte[1024];
        DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);

        while (true) {
            System.out.println("Servidor " + id + " aguardando mensagem no grupo1...");
            socketGrupo1.receive(pacoteRecepcao);

            String mensagemRecebida = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
            System.out.println("Servidor " + id + " recebeu: " + mensagemRecebida);

            filaMensagens.add(mensagemRecebida);
            System.out.println("Servidor " + id + " adicionou mensagem na fila.");
        }
    }

    private void repassarMensagensParaGrupo2() throws IOException {
        int portaGrupo2 = 56791;
        MulticastSocket socketGrupo2 = new MulticastSocket();
        InetAddress multicastIPGrupo2 = InetAddress.getByName("225.7.8.10");

        while (true) {
            System.out.println("consumir");
            try {
                String mensagemParaRepassar = filaMensagens.take();
                System.out.println("Servidor " + id + " consumiu mensagem da fila: " + mensagemParaRepassar);

                byte[] bufferEnvio = mensagemParaRepassar.getBytes();
                DatagramPacket pacoteEnvio = new DatagramPacket(
                        bufferEnvio, bufferEnvio.length, multicastIPGrupo2, portaGrupo2);

                socketGrupo2.send(pacoteEnvio);
                System.out.println("Servidor " + id + " repassou mensagem para grupo2.");
            } catch (InterruptedException e) {
                System.out.println("Servidor " + id + " foi interrompido enquanto aguardava uma mensagem.");
                e.printStackTrace();
            }
        }
    }
}
