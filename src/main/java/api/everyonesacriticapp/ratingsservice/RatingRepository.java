package api.everyonesacriticapp.ratingsservice;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Aggregation;

// https://www.mongodb.com/compatibility/spring-boot

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface RatingRepository extends MongoRepository<Rating, String> {
    @Aggregation(pipeline={
        "{ '$match': { 'product_id': ?0, 'user_id': ?1, 'archived': false }}",
        "{ '$sort' : { 'created_date' : -1 }}", 
        "{ '$limit': 1 }"
    })
    Optional<Rating> findMostRecentByUserIdAndProductId(ObjectId product_id, String user_id);

    @Query("{'product_id' : ?0, 'user_id': ?1}")
    Page<Rating> findAllByUserIdAndProductId(ObjectId product_id, String user_id, Pageable pageable);

}