package server.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Receptor {
    public static void main(String[] args) throws IOException {
        int porta = 55554;
        /*
         * Criando o Multicast Socket
         */
        MulticastSocket ms = new MulticastSocket(porta);
        System.out.println("Receptor " +
                InetAddress.getLocalHost() +
                " executando na porta " +
                ms.getLocalPort());
        InetAddress multicastIP = InetAddress.getByName("225.7.8.9");
        InetSocketAddress grupo = new InetSocketAddress(multicastIP, 55555);
        NetworkInterface interfaceRede = NetworkInterface.getByName("wlp0s20f3");
        ms.joinGroup(grupo, interfaceRede);
        /*
         * Agora o MS está configurado e pronto para receber pacotes
         */
        System.out.println("Receptor " +
                InetAddress.getLocalHost() +
                " entrou no grupo endereçado por " +
                grupo);
        /*
         * Criando um DatagramPacket para recebimento
         */
        boolean flag = true;
        byte bufferRecepcao[] = new byte[1024];
        DatagramPacket pacoteRecepcao;
        while (flag) {
            pacoteRecepcao = new DatagramPacket(
                    bufferRecepcao,
                    bufferRecepcao.length);
            ms.receive(pacoteRecepcao);
            // aqui, faz-se algo útil com os dados recebidos
            System.out.println("Dados recebidos de: " +
                    pacoteRecepcao.getAddress().toString() + ":" +
                    pacoteRecepcao.getPort() + " com tamanho: " +
                    pacoteRecepcao.getLength());
            System.out.write(
                    bufferRecepcao,
                    0,
                    pacoteRecepcao.getLength());
            System.out.println();
        }
        ms.leaveGroup(grupo, interfaceRede);
        ms.close();
    }
}
