package client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;
import static client.ClientRequests.*;

public class ClientThread implements Runnable{

    private String ip;
    private int port;

    public ClientThread(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        DatagramSocket clientSocket;
        BufferedReader br;
        int clientPort;
        Set<String> recievedPackets;


         /*
         Connect: enviar os arquivos pro server e abrir o socket.
         Get: Cliente que quer os arquivos escolhe um e inicia uma conversa com o outro socket.
          */

        try {
            clientSocket = new DatagramSocket(port, InetAddress.getByName(ip));
            clientSocket.setSoTimeout(500);
            br = new BufferedReader(new InputStreamReader(System.in));
            recievedPackets = new HashSet<>();
            while (!Thread.currentThread().isInterrupted()) {

                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                clientSocket.receive(dp);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(buf);
                ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(byteStream));

                Object obj = is.readObject();

                if (obj instanceof RequestPacket) {

                    send((RequestPacket) obj, clientSocket);
                } else if (obj instanceof FilePacket) {
                    System.out.println("aaaa");
                }

                System.out.println("Client has started on port " + port);

            }
        } catch (Exception e){
            System.err.println("Opa, algo deu errado!");
            System.exit(1);
        }
    }
}
