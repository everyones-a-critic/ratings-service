package api.everyonesacriticapp.ratingsservice;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class Rating {

    @Id
    public String id;
    private ObjectId product_id;
    public String user_id;
    public Double rating;
    public String comments;
    public Boolean archived;
    private String created_by_id;
    public Instant created_date;
    private String modified_by_id;
    public Instant modified_date;

    public Rating() {}

    public Rating(
        String id, ObjectId product_id, String user_id, Double rating, String comments, Boolean archived,
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comments = comments;
        this.archived = archived;
        this.created_by_id = created_by_id;
        this.created_date = created_date;
        this.modified_by_id = modified_by_id;
        this.modified_date = modified_date;
    }

    public Rating(
        ObjectId product_id, String user_id, Double rating, String comments,
        String created_by_id, Instant created_date, String modified_by_id, Instant modified_date
    ) {
        this.id = null;
        this.product_id = product_id;
        this.user_id = user_id;
        this.rating = rating;
        this.comments = comments;
        this.archived = false;
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

    public String getProduct_id() {
        return product_id.toString();
    }

}