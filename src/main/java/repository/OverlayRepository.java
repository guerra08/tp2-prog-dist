package repository;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;

public class OverlayRepository {

    private Map<String, LocalTime> overlayKeeper;
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
            if(Duration.between(entry.getValue(), LocalTime.now()).toMillis() > 10000){
                overlayKeeper.remove(entry.getKey());
            }
        }
    }

}
