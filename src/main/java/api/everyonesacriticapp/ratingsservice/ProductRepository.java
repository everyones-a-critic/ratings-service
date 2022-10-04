package api.everyonesacriticapp.ratingsservice;

// https://www.mongodb.com/compatibility/spring-boot

import org.springframework.data.mongodb.repository.MongoRepository;


public interface ProductRepository extends MongoRepository<Product, String> {}