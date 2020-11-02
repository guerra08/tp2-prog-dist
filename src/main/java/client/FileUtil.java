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
import java.util.Arrays;

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
            byte[] allBytesOfFile = Files.readAllBytes(Paths.get(rp.getFileName()));
            int parts = allBytesOfFile.length / 512;
            if (parts == 0) {
                FilePacket file = new FilePacket(rp.getFileName(), allBytesOfFile, rp.getPort(), rp.getIp(), true);
                filePackets.add(file);
            } else {
                int byteStart = 0;
                for (int i = 0; i <= parts; i++) {
                    int byteEnd = Math.min(byteStart + 512, allBytesOfFile.length);
                    FilePacket file = new FilePacket(rp.getFileName(), Arrays.copyOfRange(allBytesOfFile, byteStart, byteEnd), rp.getPort(), rp.getIp(), i == parts);
                    filePackets.add(file);
                    byteStart += 512;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filePackets);
        return filePackets;
    }

    public static void mountFileFromPackets(ArrayList<FilePacket> packets) {
        try{
            String pathFile = packets.get(0).getFileName();
            String fileName  = pathFile.substring(pathFile.lastIndexOf(File.separator) + 1);
            File fileToCreate = new File(Config.receiveDir + File.separator + fileName);
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
