package api.everyonesacriticapp.ratingsservice;


public class RatingRequestModel {
    public Double rating;
    public String comments;
    public Boolean archived;
    
    public RatingRequestModel(Double rating, String comments) {
        this.rating = rating;
        this.comments = comments;
    }

    public RatingRequestModel(Double rating) {
        this.rating = rating;
        this.comments = null;
    }

    public RatingRequestModel(Boolean archived) {
        this.archived = archived;
    }

}