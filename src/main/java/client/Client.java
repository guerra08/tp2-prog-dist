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

    public Client(String ip, Integer port, String server) {
        this.port = port;
        this.ip = ip;
        this.server = server;
        this.clientThread = new Thread(new ClientThread(ip, port));
        this.restConsumer = new Timer();
    }

    private List<Path> filesToSend;
    private final Scanner sc = new Scanner(System.in);
    private Timer restConsumer;
    private Thread clientThread;

    private final Integer port;
    private final String ip;
    private final String server;
    private boolean isConnected = false;

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Usage: java Client <client-ip> <client-port> <server-address>");
        }
        else{
            Client c = new Client(args[0], Integer.parseInt(args[1]), args[2]);
            c.handleInputs();
        }
    }

    private void handleInputs() {
        System.out.println(
                "Digite connect para se conectar ao servidor e enviar a sua lista de recursos\n" +
                        "Digite local para exibir todos os arquivos locais e selecioná-los\n" +
                        "Digite resources para mostrar todos os recursos disponíveis\n" +
                        "Digite peers para mostrar todos os peers disponíveis\n" +
                        "Digite get + id de um recurso\n" +
                        "Digite exit para finalizar\n");
        String choice = sc.next();
        while (!choice.equals("exit")) {
            switch(choice) {
                case "connect": {
                    if (!isConnected && connect(ip, port, server, filesToSend)) {
                        restConsumer.schedule((new OverlayTask(ip, port, server)), 0,5000);
                        clientThread.start();
                        isConnected = true;
                    }
                    break;
                }
                case "local":        if(!isConnected) filesToSend = getInputFiles(); break;
                case "resources":   index(server, "/resources"); break;
                case "peers":       index(server, "/peers"); break;
                case "get": {
                    System.out.println("Digite o ID do recurso desejado: ");
                    Integer resourceId = sc.nextInt();
                    get(server, "/resources/", resourceId);
                    break;
                }
                default:            break;
            }
            choice = sc.next();
        }
        restConsumer.cancel();
        clientThread.interrupt();
    }

    private List<Path> getInputFiles() {
        try {
            List<Path> filesToSend = new ArrayList<>();
            Stream<Path> path = Files.walk(Paths.get(Config.sendDir));
            List<Path> result = path.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
            for (int i = 0; i < result.size(); i++) {
                System.out.println("Index: " + i + "\tPathh: " + result.get(i));
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
