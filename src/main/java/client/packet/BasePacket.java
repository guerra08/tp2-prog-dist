package client.packet;

import java.io.Serializable;

public abstract class BasePacket implements Serializable {

    private String destinationIp;
    private int destinationPort;

    public BasePacket(String destinationIp, int destinationPort) {
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
    }

    public BasePacket(){}

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