package server.grupoFechado;

import java.io.IOException;
import java.util.Random;

public class App {
    public static void main(String[] args) throws IOException,
            InterruptedException {
        new Processo(new Random().nextInt(100) + 1).init();
    }
}
