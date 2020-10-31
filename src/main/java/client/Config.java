package client;

import java.io.File;

public class Config {
    //Directory with files to be sent
    public static String sendDir      = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "send";
    //Directory with files received
    public static String receiveDir   = System.getProperty("user.home") + File.separator + "prog-dist-resources" + File.separator + "receive";
}
