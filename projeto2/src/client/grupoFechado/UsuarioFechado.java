package client.grupoFechado;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;


public class UsuarioFechado {

    public void init() throws IOException {
        int porta = 56791;
        InetAddress multicastIP = InetAddress.getByName("225.7.8.10");
        MulticastSocket socket = new MulticastSocket(porta);

        socket.joinGroup(new InetSocketAddress(multicastIP, porta), null);
        System.out.println("Usuário conectado ao grupo2.");

        byte[] bufferRecepcao = new byte[1024];
        DatagramPacket pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);

        while (true) {
            socket.receive(pacoteRecepcao);
            String mensagemRecebida = new String(pacoteRecepcao.getData(), 0, pacoteRecepcao.getLength());
            System.out.println("Usuário recebeu: " + mensagemRecebida);
        }
    }

    public static void main(String[] args) {
        try {
            UsuarioFechado usuarioFechado = new UsuarioFechado();
            usuarioFechado.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
