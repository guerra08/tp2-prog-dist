package server;

import domain.Resource;
import repository.ResourceRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/resources/*")
public class ResourcesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestUrl = request.getRequestURI();
        String[] splitUrl = requestUrl.split("/");
        if(splitUrl.length == 2){
            response.getOutputStream().println(ResourceRepository.getInstance().getAllResources());
        }
        else if(splitUrl.length == 3){
            try{
                int resourceId = Integer.parseInt(splitUrl[2]);
                Resource r = ResourceRepository.getInstance().getResourceById(resourceId);
                if(r == null){
                    response.setStatus(404);
                    response.getOutputStream().println("Not found.");
                }
                else{
                    response.getOutputStream().println(r.toString());
                }
            }catch (Exception e){
                response.setStatus(400);
            }
        }
    }

}
