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

    /**
     * Puts a Peer in the repository, alongside the current time.
     * @param peer String
     */
    public void putPeer(String peer){
        this.overlayKeeper.put(peer, LocalTime.now());
    }

    /**
     * Checks if the repository is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return this.overlayKeeper.isEmpty();
    }

    /**
     * Checks all the current Peers and removes any that have not sent a PUT request to /peers/overlay in the last 10 seconds.
     */
    public void checkAllConnections(){
        for(Map.Entry<String, LocalTime> entry : overlayKeeper.entrySet()){
            if(Duration.between(entry.getValue(), LocalTime.now()).getSeconds() > 10){
                overlayKeeper.remove(entry.getKey());
                ResourceRepository.getInstance().removePeer(entry.getKey());
            }
        }
    }

    /**
     * Returns the connected Peer count
     * @return int
     */
    public int getConnectedPeersCount(){
        return overlayKeeper.size();
    }

}
