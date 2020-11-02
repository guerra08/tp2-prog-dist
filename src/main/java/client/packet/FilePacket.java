package client.packet;

public class FilePacket extends BasePacket {

    private String fileName;
    private byte[] buff;
    private boolean lastPacket;

    public FilePacket(String fileName, byte[] buff, int port, String ip, boolean lastPacket) {
        super(ip, port);
        this.fileName = fileName;
        this.buff = buff;
        this.lastPacket = lastPacket;
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

    public boolean isLastPacket() {
        return lastPacket;
    }

    public void setLastPacket(boolean lastPacket) {
        this.lastPacket = lastPacket;
    }
}
