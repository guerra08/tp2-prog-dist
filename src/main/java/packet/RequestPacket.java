package packet;

public class RequestPacket extends BasePacket {
    private String fileName;
    private String ip;
    private int port;
    private String destinationIp;
    private int destinationPort;

    public RequestPacket(String fileName, String ip, int port, String destinationIp, int destinationPort) {
        this.fileName = fileName;
        this.ip = ip;
        this.port = port;
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(String destinationIp) {
        this.destinationIp = destinationIp;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

}
