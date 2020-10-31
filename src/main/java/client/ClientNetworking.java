package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.PeerPostBody;
import domain.Resource;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ClientNetworking {

    /**
     * Connects a Client (peer) to the main server.
     * @param ip String Client IP
     * @param port int Client PORT
     * @param filesToSend List<Path> Files to be registered
     * @return boolean
     */
    public static boolean connect(String ip, int port, String server, List<Path> filesToSend) {
        try {
            System.out.println("Os seguintes arquivos serão enviados para o servidor: " + filesToSend);
            List<Resource> resourceList = new ArrayList<>();
            filesToSend.forEach(file -> resourceList.add(new Resource(file.toString(), "hash123")));
            PeerPostBody peerPostBody = new PeerPostBody(ip, port, resourceList);
            String peerPostBodyJSON = new ObjectMapper().writeValueAsString(peerPostBody);
            HTTPResponse response = httpRequest(server + "/peers", "POST", peerPostBodyJSON);
            if(response != null && response.getResponseCode() == 200){
                System.out.println(response.getResponseMessage());
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void index(String server, String route) {
        HTTPResponse response = httpRequest(server + route, "GET", null);
        if(response != null){
            System.out.println(response.getResponseMessage());
        }
    }

    public static void get() { }

    /**
     * Sends a packet through a socket
     * @param packet RequestPacket
     * @param socket DatagramSocket
     */
    public static void sendPacket(RequestPacket packet, DatagramSocket socket) {
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

    /**
     * Performs an HTTP request to a specific URL
     * @param url String URL
     * @param method String HTTP METHOD
     * @param body String request body (JSON)
     * @return HTTPResponse
     */
    public static HTTPResponse httpRequest(String url, String method, String body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(method);
            if(method.equals("POST") || method.equals("PUT")){
                connection.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(body);
                wr.flush();
                return new HTTPResponse(connection.getResponseCode(), connection.getResponseMessage());
            }
            else{
                return new HTTPResponse(connection.getResponseCode(),
                        new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
