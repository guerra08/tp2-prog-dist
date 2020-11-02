package repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Resource;

import java.util.*;

public class ResourceRepository {

    private final Map<String, List<Resource>> resourceStore = new HashMap<>();
    private int currentResourceId = 0;
    private static ResourceRepository instance;
    private final ObjectMapper mapper = new ObjectMapper();

    private ResourceRepository(){}

    public static ResourceRepository getInstance(){
        if(instance == null)
            instance = new ResourceRepository();
        return instance;
    }

    /**
     * Checks if a Peer is already registered in the server
     * @param peerIp String
     * @return boolean
     */
    public boolean isPeerRegistered(String peerIp){
        return resourceStore.containsKey(peerIp);
    }

    /**
     * Adds a Peer and it's optional resources
     * @param peerIp String
     * @param port Integer
     * @param resources List<Resource>
     * @return boolean
     */
    public boolean addPeer(String peerIp, Integer port, List<Resource> resources){
        List<Resource> toSave = new ArrayList<>();
        for(Resource r : resources){
            r.setId(currentResourceId++);
            r.setPeerIp(peerIp);
            r.setPeerPort(port);
            toSave.add(r);
        }
        resourceStore.put(peerIp + ":" + port, toSave);
        return true;
    }

    /**
     * Adds a Resource of a Peer
     * @param peerIp String
     * @param resource Resource
     */
    public void addResource(String peerIp, Resource resource){
        resource.setId(currentResourceId++);
        resource.setPeerIp(peerIp);
        List<Resource> resourcesList = resourceStore.get(peerIp);
        resourcesList.add(resource);
        resourceStore.put(peerIp, resourcesList);
    }

    /**
     * Returns an String representation of all the Resources grouped by it's owner (Peer)
     * @return String
     */
    public String getAllResourcesGroupByPeer(){
        StringBuilder peerAndResources = new StringBuilder();
        for(Map.Entry<String, List<Resource>> entry : resourceStore.entrySet()){
            peerAndResources.append(entry.getKey()).append(" - ").append(entry.getValue().toString()).append("\n");
        }
        return peerAndResources.toString();
    }

    /**
     * Returns a Resource given it's generated ID
     * @param id int
     * @return Resource
     */
    public Resource getResourceById(int id){
        Resource res = null;
        for(List<Resource> list : resourceStore.values()){
            res = list.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        }
        return res;
    }

    /**
     * Returns a String representation of all Resources
     * @return String
     */
    public String getAllResources(){
        StringBuilder ret = new StringBuilder();
        for(List<Resource> list : resourceStore.values()){
            for(Resource r : list){
                ret.append(r.toString()).append("\n");
            }
        }
        return ret.toString();
    }

    /**
     * Removes a Peer and it's Resources from the repository
     * @param peer String
     */
    public void removePeer(String peer){
        resourceStore.remove(peer);
    }

    public int getSize(){
        return resourceStore.size();
    }
}
