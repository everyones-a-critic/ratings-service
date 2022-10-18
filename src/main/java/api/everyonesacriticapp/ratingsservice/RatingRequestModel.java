package api.everyonesacriticapp.ratingsservice;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


public class RatingRequestModel {
    public Double rating;
    public String comments;
    
    public RatingRequestModel(Double rating, String comments) {
        this.rating = rating;
        this.comments = comments;
    }

    public RatingRequestModel(Double rating) {
        this.rating = rating;
        this.comments = null;
    }

}