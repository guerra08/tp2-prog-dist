package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.OverlayPutBody;
import domain.PeerPostBody;
import org.json.JSONException;
import repository.OverlayRepository;
import repository.ResourceRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@WebServlet("/peers/*")

public class PeersServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUrl = request.getRequestURI();
        String[] splitUrl = requestUrl.split("/");
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        try{
            System.out.println("Entrou no try");
            PeerPostBody parsedBody = mapper.readValue(requestBody, PeerPostBody.class);
            if(!ResourceRepository.getInstance().isPeerRegistered("" + request.getRemotePort())){
                if(!ResourceRepository.getInstance().addPeer(parsedBody.getIp() + ":" + parsedBody.getPort(), parsedBody.getResources())){
                    response.setStatus(400);
                }
                OverlayRepository.getInstance().putPeer(parsedBody.getIp() + ":" + parsedBody.getPort());
            }
        }catch (JSONException e){
            response.setStatus(400);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUrl = request.getRequestURI();
        String[] splitUrl = requestUrl.split("/");
        System.out.println("xD");
        if(splitUrl.length == 2){
            response.getOutputStream().println(ResourceRepository.getInstance().getAllResourcesGroupByPeer());
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUrl = request.getRequestURI();
        String[] splitUrl = requestUrl.split("/");
        String requestBody = request.getReader().lines().collect(Collectors.joining());
        if(splitUrl.length == 3 && splitUrl[2].equals("overlay")){
            try{
                OverlayPutBody parsedBody = mapper.readValue(requestBody, OverlayPutBody.class);
                OverlayRepository.getInstance().putPeer(parsedBody.getIp() + ":" + parsedBody.getPort());
                response.getOutputStream().println("OK");
            }catch (JSONException e){
                response.setStatus(400);
            }
        }
    }

}
