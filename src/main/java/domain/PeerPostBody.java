package domain;

public class PeerPostBody {

    private String ip;
    private Integer port;
    private Resource[] resources;

    public PeerPostBody(){}

    public PeerPostBody(String ip, Integer port, Resource[] resources) {
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

    public Resource[] getResources() {
        return resources;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }
}
