package api.everyonesacriticapp.ratingsservice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@GetMapping("/health-check")
	public Map<String, Object> getHealthCheck() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("message", "Application is healthy");
		return jsonMap;
	}
}