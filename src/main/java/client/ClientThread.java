package client;

import packet.FilePacket;
import packet.RequestPacket;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Set;
import static client.ClientNetworking.*;

public class ClientThread implements Runnable{

    private final String ip;
    private final int port;
    private final DatagramSocket clientSocket;

    public ClientThread(String ip, int port, DatagramSocket socket) {
        this.ip = ip;
        this.port = port;
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        BufferedReader br;
        int clientPort;
        Set<String> recievedPackets;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                clientSocket.setSoTimeout(500);
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                clientSocket.receive(dp);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
                ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
                Object obj = is.readObject();
                if (obj instanceof RequestPacket) {
                    System.out.println("Request packet received from" + ((RequestPacket) obj).getPort());
                    ArrayList<FilePacket> filePackets = FileUtil.getFilePacketOfRequest(((RequestPacket) obj));
                    sendFilePackets(filePackets, clientSocket);
                } else if (obj instanceof FilePacket) {
                    ArrayList<FilePacket> filePackets = new ArrayList<>();
                    filePackets.add((FilePacket) obj);
                    FileUtil.mountFileFromPackets(filePackets);
                }
            }
            catch (IOException ignored){ }
            catch (ClassNotFoundException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
