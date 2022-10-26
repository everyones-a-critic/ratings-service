package api.everyonesacriticapp.ratingsservice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;


@Document
public class Product {

    @Id
    public String id;
    public String name;
    public String community_id;
    public String brand;
    public String image_url;
    public Double price;
    public String price_per;
    public List<String> categories;

    // product specific
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String location;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String process;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String variety;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> tasting_notes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String region;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Float abv;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    

    private String created_by_id;
    public Instant created_date;
    private String modified_by_id;
    public Instant modified_date;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Rating rating;
    
    public Product () {}

    // coffee
    public Product(
        String id, String name, String community_id, String brand, String image_url, Double price, String price_per, List<String> categories, 
        List<String> tasting_notes, String location, String process, String variety, 
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = id;
        this.name = name;
        this.community_id = community_id;
        this.brand = brand;
        this.image_url = image_url;
        this.price = price;
        this.price_per = price_per;
        this.categories = categories;
        this.tasting_notes = tasting_notes;
        this.location = location;
        this.process = process;
        this.variety = variety;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
    }

    public Product(
        String id, String name, String community_id, String brand, String image_url, Double price, String price_per, List<String> categories, 
        List<String> tasting_notes, String location, String process, String variety, 
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date,
        Rating rating
    ) {
        this.id = id;
        this.name = name;
        this.community_id = community_id;
        this.brand = brand;
        this.image_url = image_url;
        this.price = price;
        this.price_per = price_per;
        this.categories = categories;
        this.tasting_notes = tasting_notes;
        this.location = location;
        this.process = process;
        this.variety = variety;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
        this.rating = rating;
    }

    // whiskey
    public Product(
        String id, String name, String community_id, String brand, String image_url, Double price, String price_per, List<String> categories, 
        List<String> tasting_notes, String region, Float abv,
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = id;
        this.name = name;
        this.community_id = community_id;
        this.brand = brand;
        this.image_url = image_url;
        this.price = price;
        this.price_per = price_per;
        this.categories = categories;
        this.tasting_notes = tasting_notes;
        this.region = region;
        this.abv = abv;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
    }

    public Product(
        String id, String name, String community_id, String brand, String image_url, Double price, String price_per, List<String> categories, 
        List<String> tasting_notes, String region, Float abv,
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date,
        Rating rating
    ) {
        this.id = id;
        this.name = name;
        this.community_id = community_id;
        this.brand = brand;
        this.image_url = image_url;
        this.price = price;
        this.price_per = price_per;
        this.categories = categories;
        this.tasting_notes = tasting_notes;
        this.region = region;
        this.abv = abv;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
        this.rating = rating;
    }
}