package api.everyonesacriticapp.ratingsservice;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RestController
public class RatingController {
    @Autowired
    private RatingRepository repository;


	@GetMapping("/products/{product_id}/ratings")
	public Map<String, Object> getRating(@PathVariable ObjectId product_id, @RequestParam(required = false) String mostRecent,
		HttpServletRequest request, Pageable pageable
	) {
		String username = CognitoUtils.getUsername(request);
		if (username != null) {
			Page<Rating> ratingPage = new PageImpl<Rating>(new ArrayList<Rating>(), pageable, 0);	
			if (mostRecent != null && (mostRecent.toLowerCase().equals("true") || mostRecent.toLowerCase().equals("yes") || mostRecent.equals("1"))) {
				Optional<Rating> optRating = repository.findMostRecentByUserIdAndProductId(product_id, username);
				if (optRating.isPresent()) {
					List<Rating> ratingList = new ArrayList<Rating>();
					ratingList.add(optRating.get());
					ratingPage = new PageImpl<Rating>(ratingList, pageable, 1);	
				}
			} else {
				ratingPage = repository.findAllByUserIdAndProductId(product_id, username, pageable);
			}

			// because one-indexed-parameters is set to true in application.properties
			int pageNumber = ratingPage.getNumber() + 1;
			int totalPages = ratingPage.getTotalPages();

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
			jsonMap.put("results", ratingPage.getContent());

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
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
	}
		
	
	@PostMapping(
		path="/products/{product_id}/ratings",
		consumes = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Rating> createRating(@PathVariable ObjectId product_id,
		@RequestBody(required=false) RatingRequestModel requestRating,
		HttpServletRequest request
	) {
		
		if (requestRating == null || requestRating.rating == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating is required.");
		}

		String username = CognitoUtils.getUsername(request);
		if (username != null) {
	
			Optional<Rating> optRating = repository.findMostRecentByUserIdAndProductId(product_id, username);
			if (optRating.isPresent()) {
				Rating rating = optRating.get();
				long dateDiff = Duration.between(rating.created_date, Instant.now()).toHours();
				// if it's been more than 24 hours since the rating was created, then create a new rating, 
				// otherwise, we're really just editing the same rating
				if (dateDiff < 24) {
					rating.rating = requestRating.rating;
					rating.comments = requestRating.comments;
					rating.setModifedById(username);
					rating.setModifedDate();
					Rating updatedRating = repository.save(rating);
					return ResponseEntity.status(HttpStatus.OK).body(updatedRating);
				}
			}
			
			// if not captured above, then create a new one
			Instant now = Instant.now();
			Rating createdRating = repository.save(new Rating(
				product_id,
				username,
				requestRating.rating,
				requestRating.comments,
				username,
				now,
				username,
				now
			));

			return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
	}

	@DeleteMapping("/products/{product_id}/ratings/{rating_id}")
	public ResponseEntity<Void> deleteRating(@PathVariable ObjectId product_id, @PathVariable String rating_id,
		HttpServletRequest request
	){
		repository.deleteById(rating_id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/products/{product_id}/ratings/{rating_id}")
	public ResponseEntity<Rating> deleteRating(@PathVariable ObjectId product_id, @PathVariable String rating_id,
		@RequestBody(required=false) RatingRequestModel requestRating, HttpServletRequest request
	){
		String username = CognitoUtils.getUsername(request);
		if (username == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		Optional<Rating> optRating = repository.findById(rating_id);
		if (optRating.isPresent()) {
			Rating rating = optRating.get();
			if (requestRating != null) {
				if (requestRating.rating != null) {
					rating.rating = requestRating.rating;
				}
				if (requestRating.comments != null) {
					rating.comments = requestRating.comments;
				}
				if (requestRating.archived != null) {
					rating.archived = requestRating.archived;
				}
				
				rating.setModifedById(username);
				rating.setModifedDate();
				Rating updatedRating = repository.save(rating);
				return ResponseEntity.status(HttpStatus.OK).body(updatedRating);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A patch request must include fields to update");
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}