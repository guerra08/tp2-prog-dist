package client.packet;

public class RequestPacket extends BasePacket {
    private String fileName;
    private String sourceIp;
    private int sourcePort;

    public RequestPacket(String fileName, String sourceIp, int sourcePort, String destinationIp, int destinationPort) {
        super(destinationIp, destinationPort);
        this.fileName = fileName;
        this.sourceIp = sourceIp;
        this.sourcePort = sourcePort;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(String ip) {
        this.sourceIp = ip;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int port) {
        this.sourcePort = port;
    }
}
