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
    private ArrayList<FilePacket> filePackets;

    public ClientThread(String ip, int port, DatagramSocket socket) {
        this.ip = ip;
        this.port = port;
        this.clientSocket = socket;
        filePackets = new ArrayList<>();
    }
    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                clientSocket.setSoTimeout(2000);
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                clientSocket.receive(dp);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
                ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));
                Object obj = is.readObject();
                if (obj instanceof RequestPacket) {
                    ArrayList<FilePacket> files = FileUtil.getFilePacketOfRequest(((RequestPacket) obj));
                    sendFilePackets(files, clientSocket);
                } else if (obj instanceof FilePacket) {
                    filePackets.add((FilePacket) obj);
                    System.out.println(((FilePacket) obj).getFileName());
                    if (((FilePacket) obj).isLastPacket()) {
                        FileUtil.mountFileFromPackets(filePackets);
                    }
                }
            }
            catch (IOException ignored){
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
