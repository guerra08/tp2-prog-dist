package domain;

import java.util.List;

public class PeerPostBody {

    private String ip;
    private Integer port;
    private List<Resource> resources;

    public PeerPostBody(){}

    public PeerPostBody(String ip, Integer port, List<Resource> resources) {
        this.ip = ip;
        this.port = port;
        this.resources = resources;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
