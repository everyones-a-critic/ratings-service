package api.everyonesacriticapp.ratingsservice;

import org.springframework.data.domain.Pageable;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

// https://www.mongodb.com/compatibility/spring-boot

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{'community_id' : ?0}")
    Page<Product> findByCommunityId(ObjectId community_id, Pageable pageable);
}