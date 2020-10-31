package domain;

public class Resource {

    private Integer id;
    private String name;
    private String hash;
    private String peerIp;
    private Integer peerPort;

    public Resource(){}

    public Resource(String name, String hash) {
        this.name = name;
        this.hash = hash;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPeerIp() { return peerIp; }

    public void setPeerIp(String peerIp) { this.peerIp = peerIp; }

    public Integer getPeerPort() { return peerPort; }

    public void setPeerPort(Integer peerPort) { this.peerPort = peerPort; }

    public String toString(){
        return "ID: " + this.id + " Name: " + this.name + " Hash: " + this.hash;
    }
}
