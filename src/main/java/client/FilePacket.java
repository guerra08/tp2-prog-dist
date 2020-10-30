package client;

import java.io.Serializable;

public class FilePacket implements Serializable {

    private String fileName;
    private byte buff[];

    public FilePacket(String fileName, byte[] buff) {
        this.fileName = fileName;
        this.buff = buff;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getBuff() {
        return buff;
    }

    public void setBuff(byte[] buff) {
        this.buff = buff;
    }

}
