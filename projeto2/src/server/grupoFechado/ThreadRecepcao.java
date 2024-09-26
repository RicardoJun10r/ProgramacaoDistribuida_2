package server.grupoFechado;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class ThreadRecepcao implements Runnable {
    private MulticastSocket multicastSocket = null;

    public ThreadRecepcao(MulticastSocket s) {
        this.multicastSocket = s;
    }

    @Override
    public void run() {
        boolean flag = true;
        byte dadosRecepcao[] = new byte[1024];
        DatagramPacket pacoteRecepcao;
        try {
            while (flag) {
                pacoteRecepcao = new DatagramPacket(
                        dadosRecepcao,
                        dadosRecepcao.length);
                multicastSocket.receive(pacoteRecepcao);
                // aqui, faz-se algo útil com os dados recebidos
                System.out.println("\n*** Dados recebidos de: " +
                        pacoteRecepcao.getAddress().toString() +
                        ":" + pacoteRecepcao.getPort() +
                        " com tamanho: " +
                        pacoteRecepcao.getLength());
                System.out.write(dadosRecepcao, 0,
                        pacoteRecepcao.getLength());
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * Fechando o MS
         */
        multicastSocket.close();
    }
}
