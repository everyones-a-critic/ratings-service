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
	public Map<String, Object> listProducts(
		@RequestParam(required = false) ObjectId communityId, @RequestParam(required = false) String withRatings, 
		HttpServletRequest request, Pageable pageable
	) {
		String username = CognitoUtils.getUsername(request);
		if (username == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		} 

		Page<Product> results;
		if (communityId != null) {
			if (withRatings != null && (withRatings.toLowerCase().equals("true") || withRatings.toLowerCase().equals("yes") || withRatings.equals("1"))) {
				results = repository.findAllByCommunityIdWithRatings(communityId, username, pageable);
			} else {
				results = repository.findAllByCommunityId(communityId, pageable);
			}
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
		
		String pathInfo = request.getPathInfo();
		String servletPath = request.getServletPath();
		String requestURL = "";
		if (servletPath != null) {
			requestURL += servletPath;
		}
		if (pathInfo != null) {
			requestURL += pathInfo;
		}
		requestURL = requestURL + "?" + queryString + queryParamSep;
		
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