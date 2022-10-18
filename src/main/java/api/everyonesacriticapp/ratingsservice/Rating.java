package api.everyonesacriticapp.ratingsservice;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Rating {

    @Id
    public String id;
    public String product_id;
    public String user_id;
    public Double rating;
    public String comments;
    private String created_by_id;
    public Instant created_date;
    private String modified_by_id;
    public Instant modified_date;

    public Rating() {}

    public Rating(
        String id, String product_id, String user_id, Double rating, String comments, 
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comments = comments;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
    }

    public Rating(
        String product_id, String user_id, Double rating, String comments, 
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = null;
        this.product_id = product_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comments = comments;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
    }
    
    public void setModifedById(String userId) {
        this.modified_by_id = userId;
    }

    public void setModifedDate() {
        this.modified_date = Instant.now();
    }
    
    public void setCreatedDate() {
        this.created_date = Instant.now();
    }

}