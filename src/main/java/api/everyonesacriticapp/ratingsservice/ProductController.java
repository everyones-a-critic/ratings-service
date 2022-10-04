package api.everyonesacriticapp.ratingsservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository repository;

	@GetMapping("/products")
	public Map<String, Object> getProducts() {
        List<Product> results = repository.findAll();
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("results", results);
		jsonMap.put("next", null);
		jsonMap.put("previous", null);
		return jsonMap;
	}
}