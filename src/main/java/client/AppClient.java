package client;

import java.io.File;

public class AppClient {

    public static void main(String[] args) {
        String sendDir      = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "send";
        String receiveDir   = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "receive";
    }

}
