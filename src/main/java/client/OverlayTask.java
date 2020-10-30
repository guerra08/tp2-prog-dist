package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.OverlayPutBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static client.ClientRequests.*;

public class OverlayTask extends TimerTask {

    private String ip;
    private Integer port;

    public OverlayTask(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public void consumeRest() throws IOException {
        String url = "http://localhost:8080/peers/overlay";
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
