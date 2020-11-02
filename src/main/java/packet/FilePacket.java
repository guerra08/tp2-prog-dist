package packet;

public class FilePacket extends BasePacket {

    private String fileName;
    private byte[] buff;
    private int port;
    private String ip;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public FilePacket(String fileName, byte[] buff, int port, String ip) {
        this.fileName = fileName;
        this.buff = buff;
        this.port = port;
        this.ip = ip;
    }

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
