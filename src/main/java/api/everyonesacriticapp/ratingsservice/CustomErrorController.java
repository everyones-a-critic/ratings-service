package api.everyonesacriticapp.ratingsservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public Map<String, Object> handleError(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
                
        if (message != null && message != "") {
            jsonMap.put("message", message);
        } else {
            jsonMap.put("message", "An error has occurred.");
        }
        if (status != null) {
            if (status.toString().equals("404")) {
                jsonMap.put("message", "The requested resource was not found.");
            }
        }

        return jsonMap;
    }
}