package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static client.ClientRequests.*;

public class AppClient {

    public static List<Path> filesToSend = new ArrayList<>();
    public static Scanner sc = new Scanner(System.in);

    public static String sendDir      = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "send";
    public static String receiveDir   = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "receive";

    public static void main(String[] args) {

        // abre o socket
        handleInputs();

        /*
        // Consumes REST for overlay on background every 5 seconds
        Timer restConsumer = new Timer();
        restConsumer.schedule(new OverlayTask(), 0,5000);

         */
    }

    public static void handleInputs() {
        System.out.println(
                "Digite connect para se conectar ao servidor e enviar a sua lista de recursos\n" +
                        "Digite list para exibir todos os arquivos e selecioná-los\n" +
                        "Digite index para mostrar todos os recursos disponíveis\n" +
                        "Digite get + id de um recurso\n" +
                        "Digite exit para finalizar\n");

        Character choice = sc.next().charAt(0);
        while (!choice.equals('e')) {
            switch(choice) {
                case 'c': connect(); break;
                case 'l': list(sendDir); break;
                case 'i': index(); break;
                case 'g': get(); break;
                default: break;
            }
            choice = sc.next().charAt(0);
        }
    }

}
