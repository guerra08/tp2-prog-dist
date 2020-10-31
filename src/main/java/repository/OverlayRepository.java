package repository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class OverlayRepository {

    private final Map<String, LocalTime> overlayKeeper = new HashMap<>();
    private static OverlayRepository instance;

    private OverlayRepository(){}

    public static OverlayRepository getInstance(){
        if(instance == null)
            instance = new OverlayRepository();
        return instance;
    }

    public void putPeer(String peer){
        this.overlayKeeper.put(peer, LocalTime.now());
    }

    public boolean isEmpty(){
        return this.overlayKeeper.isEmpty();
    }

    public void checkAllConnections(){
        for(Map.Entry<String, LocalTime> entry : overlayKeeper.entrySet()){
            if(Duration.between(entry.getValue(), LocalTime.now()).getSeconds() > 10){
                overlayKeeper.remove(entry.getKey());
                ResourceRepository.getInstance().removePeer(entry.getKey());
            }
        }
    }

    public int getConnectedPeersCount(){
        return overlayKeeper.size();
    }

}
