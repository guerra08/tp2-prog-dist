package client;

import java.io.File;

public class Config {
    public static String sendDir      = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "send";
    public static String receiveDir   = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "receive";
}
