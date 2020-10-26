package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class OverlayTask extends TimerTask {

    public static void consumeRest() throws IOException {
        try{
            URL servletUrl = new URL("http://localhost:8080/peers/overlay");
            BufferedReader br = new BufferedReader(new InputStreamReader(servletUrl.openStream()));
            String data = br.lines().collect(Collectors.joining());
            System.out.println(data);
            br.close();
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

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
