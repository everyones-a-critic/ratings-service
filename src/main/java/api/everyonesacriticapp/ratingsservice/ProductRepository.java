package api.everyonesacriticapp.ratingsservice;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Aggregation;

// https://www.mongodb.com/compatibility/spring-boot

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;


public interface ProductRepository extends MongoRepository<Product, String> {
    @Query("{'community_id' : ?0}")
    Page<Product> findAllByCommunityId(ObjectId community_id, Pageable pageable);

    @Aggregation(pipeline={
        "{'$lookup': {" +
        "   'from': 'rating', 'localField': '_id', 'foreignField': 'product_id', 'let': {'product_community_id': '$community_id'}," +
        "   'pipeline': [" +
        "       {'$match': {'$expr': {'$and': [" +
        "           {'$eq': ['$user_id', ?1]}," +
        "           {'$eq': ['$archived', false]}," +
        "           {'$eq': ['$$product_community_id', ?0]}" +
        "       ]}}}," +
        "       {'$limit': 1} "+
        "   ]," +
        "   'as': 'rating'" +
        "}}",
        "{'$match': {'rating': {'$ne': []}}}",
        "{'$sort': {'rating.created_date': -1}}",
        "{'$limit': ?2 }",
        "{'$skip': ?3 }",
        "{'$unwind': {'path': '$rating'}}"
    })
    ArrayList<Product> findAllByCommunityIdWithRatings(ObjectId community_id, String user_id, Long limit, Long skip);
}

