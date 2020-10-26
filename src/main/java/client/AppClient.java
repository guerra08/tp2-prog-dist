package client;

import java.io.File;
import java.util.Timer;

public class AppClient {

    public static void main(String[] args) {
        String sendDir      = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "send";
        String receiveDir   = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "receive";

        // Consumes REST for overlay on background every 5 seconds
        Timer restConsumer = new Timer();
        restConsumer.schedule(new OverlayTask(), 0,5000);
    }

}
