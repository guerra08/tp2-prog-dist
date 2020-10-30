package client;

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

import static client.ClientNetworking.*;

public class Client {

    public Client(Integer port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    private List<Path> filesToSend;
    private final Scanner sc = new Scanner(System.in);

    private final Integer port;
    private final String ip;

    public static void main(String[] args) {
        Client c = new Client(Integer.parseInt(args[1]), args[0]);
        c.handleInputs();
    }

    private void handleInputs() {
        System.out.println(
                "Digite connect para se conectar ao servidor e enviar a sua lista de recursos\n" +
                        "Digite list para exibir todos os arquivos e selecioná-los\n" +
                        "Digite index para mostrar todos os recursos disponíveis\n" +
                        "Digite get + id de um recurso\n" +
                        "Digite exit para finalizar\n");

        Character choice = sc.next().charAt(0);
        while (!choice.equals('e')) {
            switch(choice) {
                case 'c': {
                    if (connect(ip, port, filesToSend)) {
                        ClientThread clientThread = new ClientThread(ip, port);
                        new Thread(clientThread).start();
                        Timer restConsumer = new Timer();
                        restConsumer.schedule(new OverlayTask(ip, port), 0,5000);
                    }
                }
                break;
                case 'l': filesToSend = getInputFiles(); break;
                case 'i': index(); break;
                case 'g': get(); break;
                default: break;
            }
            choice = sc.next().charAt(0);
        }
    }

    private List<Path> getInputFiles() {
        try {
            List<Path> filesToSend = new ArrayList<>();
            Stream<Path> path = Files.walk(Paths.get(Config.sendDir));
            List<Path> result = path.filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (int i = 0; i < result.size(); i++) {
                System.out.println("Index: " + i + "\tPath: " + result.get(i));
            }

            int choice;
            while (true) {
                System.out.println("Selecione um arquivo\n-1 para finalizar a seleção de arquivos");
                choice = sc.nextInt();
                try {
                    if (choice == -1) {
                        break;
                    }
                    if (choice == 99) {
                        filesToSend.addAll(result);
                    }
                    filesToSend.add(result.get(choice));
                    result.remove(choice);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Seleção de arquivos finalizada.");
            return filesToSend;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
