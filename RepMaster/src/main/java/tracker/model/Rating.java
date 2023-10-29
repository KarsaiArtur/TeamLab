package tracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Rating {
    private List<RatingComment> allRatings;
    private double rating;

    @Getter
    @Setter
    private class RatingComment{
        String comment;
        int rating;

        public RatingComment(String comment, int rating){
            this.comment = comment;
            this.rating = rating;
        }
    }

    public void addRating(int rating, String comment) {
        if(allRatings == null) allRatings = new ArrayList<>();
        allRatings.add(new RatingComment(comment, rating));
        calculateRating();
    }

    public void calculateRating() {
        int ratingsCnt = 0;
        int sum = 0;
        for(RatingComment currentRating : allRatings) {
            ratingsCnt++;
            sum += currentRating.getRating();
        }
        this.rating = sum / (double)ratingsCnt;
    }
}
