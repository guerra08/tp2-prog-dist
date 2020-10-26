package repository;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    public boolean addPeer(String peerIp, String requestBody){
        Resource[] peerResources;
        try {
            peerResources = mapper.readValue(requestBody, Resource[].class);
            List<Resource> toSave = new ArrayList<>();
            for(Resource r : peerResources){
                r.setId(currentResourceId++);
                r.setPeerIp(peerIp);
                toSave.add(r);
            }
            resourceStore.put(peerIp, toSave);
            System.out.println(resourceStore);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addResource(String peerIp, Resource resource){
        resource.setId(currentResourceId++);
        resource.setPeerIp(peerIp);
        List<Resource> resourcesList = resourceStore.get(peerIp);
        resourcesList.add(resource);
        resourceStore.put(peerIp, resourcesList);
    }

    public String getAllResourcesByPeer(){
        StringBuilder peerAndResources = new StringBuilder();
        for(Map.Entry<String, List<Resource>> entry : resourceStore.entrySet()){
            peerAndResources.append(entry.getKey()).append(" - ").append(entry.getValue().toString()).append("\n");
        }
        return peerAndResources.toString();
    }

}
