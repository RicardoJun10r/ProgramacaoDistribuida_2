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

        ms.joinGroup(grupo, interfaceRede);
        System.out.println("Receptor entrou no grupo endereçado por " + grupo);

        boolean flag = true;
        byte bufferRecepcao[] = new byte[1024];
        DatagramPacket pacoteRecepcao;
        String line;
        BlockingQueue<String> fila = new LinkedBlockingQueue<>();
        // HashMap para armazenar IP e porta dos clientes
        HashMap<InetAddress, Integer> usuarios = new HashMap<>();

        while (flag) {
            pacoteRecepcao = new DatagramPacket(bufferRecepcao, bufferRecepcao.length);
            ms.receive(pacoteRecepcao);

            System.out.println("Dados recebidos de: " + pacoteRecepcao.getAddress().toString() + ":" +
                    pacoteRecepcao.getPort() + " com tamanho: " + pacoteRecepcao.getLength());
            line = new String(bufferRecepcao, 0, pacoteRecepcao.getLength());
            System.out.println("DEBUG: " + line);
            
            // Verifica se a mensagem é de um drone
            if (line.contains("DRONE")) {
                System.out.println(line.substring(5));
                fila.add(line.substring(5));  // Armazena os dados climáticos recebidos do drone
            } 
            // Verifica se a mensagem é de um usuário
            else if (line.contains("USUARIO")) {
                // Adiciona o usuário ao HashMap se não estiver presente
                if (!usuarios.containsKey(pacoteRecepcao.getAddress())) {
                    usuarios.put(pacoteRecepcao.getAddress(), pacoteRecepcao.getPort());
                }

                // Verifica se há dados na fila para enviar
                if (!fila.isEmpty()) {
                    String mensagemParaUsuario = fila.take();  // Obtém o primeiro dado, mas não remove da fila
                    byte[] bufferEnvio = mensagemParaUsuario.getBytes();

                    // Obtém o IP e porta do usuário que solicitou
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
