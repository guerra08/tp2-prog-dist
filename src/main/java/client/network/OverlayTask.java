package client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.OverlayPutBody;

import java.io.IOException;
import java.util.TimerTask;

import static client.network.ClientNetworking.*;

public class OverlayTask extends TimerTask {

    private final String ip;
    private final Integer port;
    private final String server;

    /**
     * OverlayTask
     * @param ip String Peer IP
     * @param port Integer Peer Port
     * @param server String Server address
     */
    public OverlayTask(String ip, Integer port, String server) {
        this.ip = ip;
        this.port = port;
        this.server = server;
    }

    /**
     * Consumes the overlay PUT method (keeps the peer alive)
     * @throws IOException When request fails
     */
    public void consumeRest() throws IOException {
        String url = server + "/peers/overlay";
        OverlayPutBody overlayBody = new OverlayPutBody(ip, port);
        String overlayBodyJson = new ObjectMapper().writeValueAsString(overlayBody);
        httpRequest(url, "PUT", overlayBodyJson);
    }

    @Override
    public void run() {
        try {
            consumeRest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
