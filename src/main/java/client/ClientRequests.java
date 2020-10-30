package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.PeerPostBody;
import domain.Resource;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientRequests {


    public static Scanner sc = new Scanner(System.in);
    public static List<Path> filesToSend = new ArrayList<>();



    public static boolean connect(String ip, int port) {
        try {
            System.out.println("Os seguintes arquivos serão enviados para o servidor: " + filesToSend);

            List<Resource> resourceList = new ArrayList<>();

            filesToSend.forEach(file -> {
                resourceList.add(new Resource(file.toString(), "hash123"));
            });

            PeerPostBody peerPostBody = new PeerPostBody(ip, port, resourceList);

            String peerPostBodyJSON = new ObjectMapper().writeValueAsString(peerPostBody);

            String url = "http://localhost:8080/peers";

            return httpRequest(url, "POST", peerPostBodyJSON);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void list(String sendDir) {
        try {
            Stream<Path> path = Files.walk(Paths.get(sendDir));
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void index() {
    }

    public static void get() {


    }

    public static void send(RequestPacket packet, DatagramSocket socket) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(packet.getFileName()));
            String file = br.lines().collect(Collectors.joining());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.write(file.getBytes());
            os.flush();
            byte[] sendData = bos.toByteArray();
            os.close();
            bos.close();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(packet.getIp()), packet.getPort());
            socket.send(sendPacket);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean httpRequest(String url, String method, String body) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            wr.write(body);
            wr.flush();

            int responseCode = connection.getResponseCode();
            if(responseCode == 200){
                System.out.println(method + " was successful.");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
