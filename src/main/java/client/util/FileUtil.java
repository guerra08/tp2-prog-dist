package client.util;

import client.Config;
import client.packet.FilePacket;
import client.packet.RequestPacket;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

public class FileUtil {

    /**
     * Generates the MD5 hash of a file
     * @param filePath String The file name in the config dir
     * @return String
     */
    public static String getMD5HashOfFile(String filePath){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        try (InputStream is = new FileInputStream(Config.sendDir + File.separator + filePath);
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

    /**
     * When receiving a RequestPacket, generates a List of FilePackets with the contents of the asked file.
     * @param rp RequestPacket
     * @return ArrayList<FilePacket>
     */
    public static ArrayList<FilePacket> getFilePacketOfRequest(RequestPacket rp){
        ArrayList<FilePacket> filePackets = new ArrayList<>();
        try {
            byte[] allBytesOfFile = Files.readAllBytes(Paths.get(Config.sendDir + File.separator + rp.getFileName()));
            int parts = allBytesOfFile.length / 512;
            if (parts == 0) {
                FilePacket file = new FilePacket(rp.getFileName(), allBytesOfFile, rp.getSourcePort(), rp.getSourceIp(), true);
                filePackets.add(file);
            } else {
                int byteStart = 0;
                for (int i = 0; i <= parts; i++) {
                    int byteEnd = Math.min(byteStart + 512, allBytesOfFile.length);
                    FilePacket file = new FilePacket(rp.getFileName(), Arrays.copyOfRange(allBytesOfFile, byteStart, byteEnd), rp.getSourcePort(), rp.getSourceIp(), i == parts);
                    filePackets.add(file);
                    byteStart += 512;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePackets;
    }

    /**
     * Mounts and writes to the OS FileSystem the file from an ArrayList of FilePackets
     * @param packets ArrayList<FilePacket>
     */
    public static void mountFileFromPackets(ArrayList<FilePacket> packets) {
        try{
            File fileToCreate = new File(Config.receiveDir + File.separator + packets.get(0).getFileName());
            OutputStream os = new FileOutputStream(fileToCreate);
            while(!packets.isEmpty()){
                os.write(packets.remove(0).getBuff());
            }
            os.close();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }
    }

}
