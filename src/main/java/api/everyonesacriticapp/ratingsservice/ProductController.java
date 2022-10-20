package api.everyonesacriticapp.ratingsservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
				// implementing our own pageable logic as @Aggregation doesn't support Page results
				Long pageOffset = pageable.getOffset();
				Integer pageNumber = pageable.getPageNumber() + 1;
				Integer pageSize = pageable.getPageSize();

				Long limit = pageOffset + pageSize + 1;
				Long skip = pageOffset;

				ArrayList<Product> productList = repository.findAllByCommunityIdWithRatings(communityId, username, limit, skip);
				
				// total must be greater than number of page elements in order for next page to be rendered
				long mockTotal = pageNumber  * pageSize;
				System.out.println("productList.size(): " + productList.size());
				if (productList.size() > pageSize) {
					// remove last element
					productList.remove(pageSize);
					mockTotal = pageNumber * pageSize + 1;
				}
				
				results = new PageImpl<Product>(productList, pageable, mockTotal);
			} else {
				results = repository.findAllByCommunityId(communityId, pageable);
			}
		} else {
			System.out.println("Here 1");
			results = repository.findAll(pageable);	
			System.out.println("Here 2");
		}

		// because one-indexed-parameters is set to true in application.properties
		int pageNumber = results.getNumber() + 1;
		System.out.println("pageNumber: " + pageNumber);
		int totalPages = results.getTotalPages();
		System.out.println("totalPages: " + totalPages);
		
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