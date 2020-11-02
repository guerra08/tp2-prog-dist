package client.network;

import client.util.FileUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.HTTPResponse;
import domain.PeerPostBody;
import domain.Resource;
import client.packet.BasePacket;
import client.packet.FilePacket;
import client.packet.RequestPacket;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.Collectors;

public class ClientNetworking {

    private final static ObjectMapper mapper = new ObjectMapper();

    /**
     * Connects a Client (peer) to the main server.
     * @param ip String Client IP
     * @param port int Client PORT
     * @param filesToSend List<Path> Files to be registered
     * @return boolean
     */
    public static boolean connect(String ip, int port, String server, List<String> filesToSend) {
        try {
            PeerPostBody peerPostBody;
            List<Resource> resourceList = new ArrayList<>();
            if(filesToSend != null){
                filesToSend.forEach(file -> resourceList.add(new Resource(file, FileUtil.getMD5HashOfFile(file))));
            }
            peerPostBody = new PeerPostBody(ip, port, resourceList);
            String peerPostBodyJSON = new ObjectMapper().writeValueAsString(peerPostBody);
            HTTPResponse response = httpRequest(server + "/peers", "POST", peerPostBodyJSON);
            if(response != null && response.getResponseCode() == 201){
                System.out.println("Conectado com sucesso!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Outputs the index (lists all) of a given route
     * @param server String server addr
     * @param route String route
     */
    public static void index(String server, String route) {
        HTTPResponse response = httpRequest(server + route, "GET", null);
        if(response != null){
            System.out.println(response.getResponseMessage());
        }
    }

    /**
     * Gets a resource from the server and requests it to the owner
     * @param server String server
     * @param path String path
     * @param id Integer resource id
     * @param socket DatagramSocket
     */
    public static void get(String server, String path, Integer id, DatagramSocket socket) {
        String url = server + path + id;
        HTTPResponse response = httpRequest(url, "GET", null);
        if(response != null){
            if(response.getResponseCode() == 200){
                try {
                    Resource r = mapper.readValue(response.getResponseMessage(), Resource.class);
                    sendPacket(new RequestPacket(r.getName(), socket.getLocalAddress().getHostAddress(), socket.getLocalPort(), r.getPeerIp(), r.getPeerPort()), socket);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(response.getResponseMessage());
        }
    }

    /**
     * Sends a client.packet through a socket
     * @param packet BasePacket
     * @param socket DatagramSocket
     */
    public static void sendPacket(BasePacket packet, DatagramSocket socket) {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(bos);
                os.writeObject(packet);
                os.flush();
                byte[] sendData = bos.toByteArray();
                os.close();
                bos.close();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sendData.length,
                        InetAddress.getByName(packet.getDestinationIp()),
                        packet.getDestinationPort());
                socket.send(sendPacket);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public static void sendFilePackets(ArrayList<FilePacket> filePackets, DatagramSocket socket) {
        for (FilePacket filePacket : filePackets) {
            sendPacket(filePacket, socket);
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
                if(connection.getResponseCode() == 200 || connection.getResponseCode() == 201)
                    return new HTTPResponse(connection.getResponseCode(),
                            new BufferedReader(new InputStreamReader(connection.getInputStream())).lines().collect(Collectors.joining()));
                return new HTTPResponse(connection.getResponseCode(),
                        new BufferedReader(new InputStreamReader(connection.getErrorStream())).lines().collect(Collectors.joining()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
