package server.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class Receptor {
    public static void main(String[] args) throws IOException {
        int porta = 55554;
        MulticastSocket ms = new MulticastSocket(porta);
        System.out.println("Receptor " +
                InetAddress.getLocalHost() +
                " executando na porta " +
                ms.getLocalPort());

        InetAddress multicastIP = InetAddress.getByName("225.7.8.9");
        InetSocketAddress grupo = new InetSocketAddress(multicastIP, 55555);

        NetworkInterface interfaceRede = null;

        ms.joinGroup(grupo, interfaceRede);
        System.out.println("Receptor entrou no grupo endereçado por " + grupo);

        boolean flag = true;
        byte bufferRecepcao[] = new byte[1024];
        byte bufferEnvio[] = new byte[1024];
        DatagramPacket pacoteRecepcao;
        String line;
        BlockingQueue<String> fila = new LinkedBlockingQueue<>();
        while (flag) {
            pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);
            ms.receive(pacoteRecepcao);

            System.out.println("Dados recebidos de: " + pacoteRecepcao.getAddress().toString() + ":" +
                    pacoteRecepcao.getPort() + " com tamanho: " + pacoteRecepcao.getLength());
            line = new String(bufferRecepcao);
            if(line.contains("DRONE")){
                System.out.println(line.substring(5));
                fila.add(line.substring(5));
            }
            System.out.println(fila.toString());
        }

        ms.leaveGroup(grupo, interfaceRede);
        ms.close();
    }
}
