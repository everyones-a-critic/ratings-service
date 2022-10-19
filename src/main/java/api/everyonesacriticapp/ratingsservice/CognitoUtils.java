package api.everyonesacriticapp.ratingsservice;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CognitoUtils {

    public static String getUsername(HttpServletRequest request) {
		String jwt = request.getHeader("Authorization");
        String username = null;
		if (jwt != null) {
			String[] chunks = jwt.split("\\.");

			Base64.Decoder decoder = Base64.getUrlDecoder();
			String payload = new String(decoder.decode(chunks[1]));
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = null;
			try {
				jsonNode = objectMapper.readTree(payload);
			} catch (JsonProcessingException e) {}

			if (jsonNode != null) {
				username = jsonNode.get("cognito:username").asText();
			}
		}
		return username;
	}
}
