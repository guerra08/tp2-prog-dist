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

    public boolean isPeerRegistered(String peerIp){
        return resourceStore.containsKey(peerIp);
    }

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

    public void addResource(String peerIp, Resource resource){
        resource.setId(currentResourceId++);
        resource.setPeerIp(peerIp);
        List<Resource> resourcesList = resourceStore.get(peerIp);
        resourcesList.add(resource);
        resourceStore.put(peerIp, resourcesList);
    }

    public String getAllResourcesGroupByPeer(){
        StringBuilder peerAndResources = new StringBuilder();
        for(Map.Entry<String, List<Resource>> entry : resourceStore.entrySet()){
            peerAndResources.append(entry.getKey()).append(" - ").append(entry.getValue().toString()).append("\n");
        }
        return peerAndResources.toString();
    }

    public Resource getResourceById(int id){
        Resource res = null;
        for(List<Resource> list : resourceStore.values()){
            res = list.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        }
        return res;
    }

    public String getAllResources(){
        StringBuilder ret = new StringBuilder();
        for(List<Resource> list : resourceStore.values()){
            for(Resource r : list){
                ret.append(r.toString()).append("\n");
            }
        }
        return ret.toString();
    }

    public void removePeer(String peer){
        resourceStore.remove(peer);
    }

    public int getSize(){
        return resourceStore.size();
    }
}
