package client;

import client.packet.FilePacket;
import client.packet.RequestPacket;
import client.util.FileUtil;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import static client.network.ClientNetworking.*;

public class ClientThread implements Runnable{

    private final DatagramSocket clientSocket;
    private final ArrayList<FilePacket> filePackets;

    public ClientThread(DatagramSocket socket) {
        this.clientSocket = socket;
        filePackets = new ArrayList<>();
    }

    /**
     * Receives DatagramPackets while not interrupted and processes them accordingly.
     */
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
