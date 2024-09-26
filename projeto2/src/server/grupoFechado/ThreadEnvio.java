package server.grupoFechado;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ThreadEnvio implements Runnable {
    private MulticastSocket multicastSocket = null;
    private InetAddress multicastIP = null;
    private int porta;

    public ThreadEnvio(MulticastSocket s, InetAddress mIP, int p) {
        this.multicastSocket = s;
        this.multicastIP = mIP;
        this.porta = p;
    }

    @Override
    public void run() {
        boolean flag = true;
        Scanner entrada = new Scanner(System.in);
        byte dadosEnvio[];
        DatagramPacket pacoteEnvio;
        try {
            while (flag) {
                System.out.println("\n>>> Digite a mensagem: ");
                String msg = entrada.nextLine();
                /*
                 * Inserindo dados no buffer de envio
                 */
                dadosEnvio = msg.getBytes(StandardCharsets.UTF_8);
                /*
                 * Criando um DatagramPacket para envio
                 */
                System.out.println("\nEnviando mensagem ao grupo " +
                        "multicast...");
                pacoteEnvio = new DatagramPacket(
                        dadosEnvio,
                        dadosEnvio.length,
                        InetAddress.getByName(
                                multicastIP.toString().substring(1)),
                        porta);
                /*
                 * Fazendo o envio
                 */
                multicastSocket.send(pacoteEnvio);
                if (msg.equalsIgnoreCase("sair")) {
                    flag = false;
                    System.out.println("Processo " +
                            InetAddress.getLocalHost() +
                            " finalizou sua operação.");
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        entrada.close();
        /*
         * Fechando o MS
         */
        multicastSocket.close();
    }
}
