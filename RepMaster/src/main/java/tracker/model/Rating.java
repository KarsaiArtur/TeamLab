package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int id;

    private double rating;
    private String comment;

    @ManyToOne
    private Exercise exercise;
    @ManyToOne
    private Gym gym;
    @ManyToOne
    private Workout workout;
    @ManyToOne
    private RegisteredUser registeredUser;


    public Rating(double rating, String comment){
        this.rating = rating;
        this.comment = comment;
    }

    /*public void calculateRating() {
        int ratingsCnt = 0;
        int sum = 0;
        for(RatingComment currentRating : allRatings) {
            ratingsCnt++;
            sum += currentRating.getRating();
        }
        this.rating = sum / (double)ratingsCnt;
    }*/
}

