package server.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Receptor {
    public static void main(String[] args) throws IOException, InterruptedException {
        int porta = 55554;
        MulticastSocket ms = new MulticastSocket(porta);
        System.out.println("Receptor " +
                InetAddress.getLocalHost() +
                " executando na porta " +
                ms.getLocalPort());

        InetAddress multicastIP = InetAddress.getByName("225.7.8.9");
        InetSocketAddress grupo = new InetSocketAddress(multicastIP, 55555);

        NetworkInterface interfaceRede = null;

        ms.joinGroup(grupo, null);
        System.out.println("Receptor entrou no grupo endereçado por " + grupo);

        boolean flag = true;
        byte bufferRecepcao[] = new byte[1024];
        DatagramPacket pacoteRecepcao;
        String line;
        BlockingQueue<String> fila = new LinkedBlockingQueue<>();
        HashMap<InetAddress, Integer> usuarios = new HashMap<>();

        while (flag) {
            pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);
            ms.receive(pacoteRecepcao);

            System.out.println("Dados recebidos de: " + pacoteRecepcao.getAddress().toString() + ":" +
                    pacoteRecepcao.getPort() + " com tamanho: " + pacoteRecepcao.getLength());
            line = new String(bufferRecepcao, 0, pacoteRecepcao.getLength());
            System.out.println("DEBUG: " + line);
            
            if (line.contains("DRONE")) {
                System.out.println(line.substring(5));
                fila.add(line.substring(5));
            } 
            else if (line.contains("USUARIO")) {
                if (!usuarios.containsKey(pacoteRecepcao.getAddress())) {
                    usuarios.put(pacoteRecepcao.getAddress(), pacoteRecepcao.getPort());
                }

                if (!fila.isEmpty()) {
                    String mensagemParaUsuario = fila.take();
                    byte[] bufferEnvio = mensagemParaUsuario.getBytes();

                    InetAddress ipUsuario = pacoteRecepcao.getAddress();
                    int portaUsuario = usuarios.get(ipUsuario);

                    DatagramPacket pacoteEnvio = new DatagramPacket(bufferEnvio,
                            bufferEnvio.length,
                            ipUsuario, portaUsuario);

                    ms.send(pacoteEnvio);
                    System.out.println("Enviando dados para o usuário: " + ipUsuario.toString() + ":" + portaUsuario);
                } else {
                    System.out.println("Nenhum dado disponível na fila para enviar ao usuário.");
                }
            }

            System.out.println("Fila de dados climáticos: " + fila.toString());
        }

        ms.leaveGroup(grupo, interfaceRede);
        ms.close();
    }
}
