package api.everyonesacriticapp.ratingsservice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository repository;

	@GetMapping("/products")
	public Map<String, Object> getProducts(@RequestParam(required = false) String community_id, HttpServletRequest request, Pageable pageable) {
		Page<Product> results;
		if (community_id != null) {
			ObjectId bson_id;
			try {
				bson_id = new ObjectId(community_id);
			} catch (java.lang.IllegalArgumentException e) {
				bson_id = new ObjectId();
			}

			results = repository.findByCommunityId(bson_id, pageable);
		} else {
			results = repository.findAll(pageable);	
		}

		// because one-indexed-parameters is set to true in application.properties
		int pageNumber = results.getNumber() + 1;
		int totalPages = results.getTotalPages();
		
		String queryString = request.getQueryString();
		String queryParamSep;
		if (queryString != null) {
			queryString = queryString.replace("&page=" + String.valueOf(pageNumber), "");
			queryString = queryString.replace("page=" + String.valueOf(pageNumber), "");
			queryParamSep = "&";
		} else {
			queryString = "";
			queryParamSep = "";
		}
		String requestURL = request.getRequestURL() + "?" + queryString + queryParamSep;
		
		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("results", results.getContent());

		String nextUrl = null;
		String previousUrl = null;
		if (pageNumber > 1) {
			previousUrl = requestURL + "page=" + String.valueOf(pageNumber - 1);
		}

		if (pageNumber < totalPages) {
			nextUrl = requestURL + "page=" + String.valueOf(pageNumber + 1);
		}

		jsonMap.put("next", nextUrl);
		jsonMap.put("previous", previousUrl);
		
		return jsonMap;
	}

	@GetMapping("/products/{id}")
	public Product getProduct(@PathVariable String id) {
		Optional<Product> product = repository.findById(id);
		return product.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
}