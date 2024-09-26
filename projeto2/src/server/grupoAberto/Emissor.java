package server.grupoAberto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Emissor {
    public static void main(String[] args) throws IOException {
        final int porta = 55554;
        String grupo = "225.7.8.9";
        /*
         * Criando o socket mas não vinculando-o ao grupo. Serve apenas para
         * enviar dados.
         */
        DatagramSocket ds = new DatagramSocket();
        System.out.println("Emissor " +
                InetAddress.getLocalHost() +
                " executando na porta " +
                ds.getLocalPort());/*
                                    * Não é preciso se juntar a um grupo se o processo vai apenas enviar
                                    * dados e não receber.
                                    */
        boolean flag = true;
        Scanner entrada = new Scanner(System.in);
        byte bufferEnvio[];
        DatagramPacket pacoteEnvio;
        while (flag) {
            System.out.println("Digite a mensagem: ");
            String msg = entrada.nextLine();
            /*
             * Inserindo dados no buffer de envio
             */
            bufferEnvio = msg.getBytes(StandardCharsets.UTF_8);
            /*
             * Criando um DatagramPacket para envio
             */
            System.out.println("Enviando mensagem ao grupo multicast...");
            pacoteEnvio = new DatagramPacket(
                    bufferEnvio,
                    bufferEnvio.length,
                    InetAddress.getByName(grupo),
                    porta);
            /*
             * Fazendo o envio
             */
            ds.send(pacoteEnvio);
            if (msg.equalsIgnoreCase("sair")) {
                flag = false;
                System.out.println("Emissor " +
                        InetAddress.getLocalHost() +
                        " finalizou sua operação.");
            }
        }
        /*
         * Fechando o Scanner
         */
        entrada.close();
        /*
         * Fechando o MS
         */
        ds.close();
    }
}
