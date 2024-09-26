package server.grupoFechado;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class Processo {
    private int id;

    public Processo(int n) {
        this.id = n;
    }

    public void init() throws IOException, InterruptedException {
        int porta = 56789;
        /*
         * Criando o Multicast Socket
         */
        MulticastSocket ms = new MulticastSocket(porta);
        System.out.println("Processo " +
                this.id + " -- " +
                InetAddress.getLocalHost() +
                " -- executando na porta " +
                ms.getLocalPort());
        /*
         * Se juntando ao grupo
         */
        /*
         * Forma antiga:
         * ms.joinGroup(InetAddress.getByName(group));
         */
        /**
         * Forma atual:
         */
        InetAddress multicastIP = InetAddress.getByName("225.7.8.9");
        System.out.println(multicastIP.toString().substring(1));
        InetSocketAddress grupo = new InetSocketAddress(multicastIP, 55555);
        NetworkInterface interfaceRede = NetworkInterface.getByName("wlp2s0");
        ms.joinGroup(grupo, interfaceRede);
        Thread tRecepcao = new Thread(new ThreadRecepcao(ms));
        tRecepcao.start();
        Thread tEnvio = new Thread(new ThreadEnvio(ms, multicastIP, porta));
        tEnvio.start();
    }
}
