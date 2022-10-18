package api.everyonesacriticapp.ratingsservice;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;

// https://www.mongodb.com/compatibility/spring-boot

import org.springframework.data.mongodb.repository.MongoRepository;


public interface RatingRepository extends MongoRepository<Rating, String> {
    @Aggregation(pipeline={
        "{ '$match': { 'product_id': ?0, 'user_id': ?1 }}",
        "{ '$sort' : { 'created_date' : -1 }}", 
        "{ '$limit': 1 }"
    })
    Optional<Rating> findMostRecentByUserIdAndProductId(String product_id, String user_id);

    @Aggregation(pipeline={
        "{ '$match': { 'product_id': ?0, 'user_id': ?1 }}",
        "{ '$sort' : { 'created_date' : -1 }}", 
        "{ '$limit': 1 }"
    })
    List<Rating> findAllByUserIdAndProductId(String product_id, String user_id);
}