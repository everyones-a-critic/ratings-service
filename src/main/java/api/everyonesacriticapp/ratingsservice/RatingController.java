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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.jackson.JsonObjectDeserializer;


@RestController
public class RatingController {
    @Autowired
    private RatingRepository repository;

	private static String getUsername(HttpServletRequest request) {
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

	@GetMapping("/products/{product_id}/ratings")
	public List<Rating> getRating(@PathVariable String product_id, @RequestParam(required = false) String mostRecent,
		HttpServletRequest request
	) {
		String username = getUsername(request);
		if (username != null) {
			List<Rating> ratingList = new ArrayList<Rating>();
			if (mostRecent != null && (mostRecent.toLowerCase().equals("true") || mostRecent.toLowerCase().equals("yes") || mostRecent.equals("1"))) {
				Optional<Rating> optRating = repository.findMostRecentByUserIdAndProductId(product_id, username);
				if (optRating.isPresent()) {
					ratingList.add(optRating.get());
				}
			} else {
				ratingList = repository.findAllByUserIdAndProductId(product_id, username);
			}


			return ratingList;
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		
	}
		
	
	@PostMapping(
		path="/products/{product_id}/ratings",
		consumes = {MediaType.APPLICATION_JSON_VALUE}
	)
	public ResponseEntity<Rating> createRating(@PathVariable String product_id,
		@RequestBody(required=false) RatingRequestModel requestRating,
		HttpServletRequest request
	) {
		
		if (requestRating == null || requestRating.rating == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating is required.");
		}

		String username = getUsername(request);
		if (username != null) {
	
			Optional<Rating> optRating = repository.findMostRecentByUserIdAndProductId(product_id, username);
			System.out.println("optRating.isPresent(): ");
			System.out.println(optRating.isPresent());
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
	public ResponseEntity<Void> deleteRating(@PathVariable String product_id, @PathVariable String rating_id,
		HttpServletRequest request
	){
		repository.deleteById(rating_id);
		return ResponseEntity.noContent().build();
	}

}