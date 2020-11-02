package client;

import client.network.OverlayTask;

import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static client.network.ClientNetworking.*;

public class Client {

    private List<String> filesToSend;
    private final Scanner sc = new Scanner(System.in);
    private final Timer restConsumer;
    private final Thread clientThread;
    private DatagramSocket clientSocket;
    private final Integer port;
    private final String ip;
    private final String server;
    private boolean isConnected = false;

    public Client(String ip, Integer port, String server) {
        this.port = port;
        this.ip = ip;
        this.server = server;
        try {
            this.clientSocket = new DatagramSocket(port, InetAddress.getByName(ip));
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.clientThread = new Thread(new ClientThread(clientSocket));
        this.restConsumer = new Timer();
    }

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Usage: java Client <client-ip> <client-port> <server-address>");
        }
        else{
            Client c = new Client(args[0], Integer.parseInt(args[1]), args[2]);
            c.handleInputs();
        }
    }

    /**
     * Handles the user input on the TTY and performs the respective task
     */
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
                case "resources":   {
                    if (isConnected) {
                        index(server, "/resources");
                    } else {
                        System.out.println("Conecte-se ao servidor para buscar os recursos.");
                    }
                    break;
                }
                case "peers": {
                    if (isConnected) {
                        index(server, "/peers");
                    } else {
                        System.out.println("Conecte-se ao servidor para buscar os recursos.");
                    }
                    break;
                }
                case "get": {
                    if (isConnected) {
                        System.out.println("Digite o ID do recurso desejado: ");
                        Integer resourceId = sc.nextInt();
                        get(server, "/resources/", resourceId, clientSocket);
                    } else {
                        System.out.println("Conecte-se ao server para realizar a troca de arquivos");
                    }
                    break;
                }
                default:            break;
            }
            choice = sc.next();
        }
        restConsumer.cancel();
        clientThread.interrupt();
    }

    /**
     * Returns a list of files that have been selected by the user as available on the central server.
     * @return List<String>
     */
    private List<String> getInputFiles() {
        try {
            List<String> filesToSend = new ArrayList<>();
            Stream<Path> path = Files.walk(Paths.get(Config.sendDir));
            List<Path> result = path.filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            int choice;
            while (true) {
                if (result.isEmpty()) break;
                for (int i = 0; i < result.size(); i++) {
                    System.out.println("Index: " + i + "\tPath: " + result.get(i));
                }
                System.out.println("Selecione um arquivo\n-1 para finalizar a seleção de arquivos");
                choice = sc.nextInt();
                try {
                    if (choice == -1) {
                        break;
                    } else if (choice < result.size()) {
                        filesToSend.add(result.remove(choice).getFileName().toString());
                    }

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
