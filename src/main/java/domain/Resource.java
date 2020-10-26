package domain;

public class Resource {

    private Integer id;
    private String name;
    private String hash;
    private String peerIp;

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
}
