package client;

import packet.FilePacket;
import packet.RequestPacket;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Collectors;

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

    public static ArrayList<FilePacket> getFilePacketOfRequest(RequestPacket rp){
        ArrayList<FilePacket> filePackets = new ArrayList<>();
        try {
            // RandomAccessFile raf = new RandomAccessFile(rp.getFileName(), "r");
            byte[] allBytesOfFile = Files.readAllBytes(Paths.get(rp.getFileName()));
            // raf.readFully(allBytesOfFile);
            // raf.close();
            int parts = allBytesOfFile.length / 1024;
            System.out.println("Partes: " + parts);
            if (parts == 0) {
                FilePacket file = new FilePacket(rp.getFileName(), allBytesOfFile, rp.getPort(), rp.getIp());
                filePackets.add(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePackets;
    }

}
