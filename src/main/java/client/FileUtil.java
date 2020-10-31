package client;

import packet.FilePacket;
import packet.RequestPacket;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtil {

    public static String getMD5HashOfFile(Path filePath){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        try (InputStream is = new FileInputStream(String.valueOf(filePath));
             DigestInputStream dis = new DigestInputStream(is, md)) {
            while (dis.read() != -1);
            md = dis.getMessageDigest();
            StringBuilder sb = new StringBuilder();
            for (byte b : md.digest()) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static FilePacket getFilePacketOfRequest(RequestPacket rp){

        return null;
    }

}
