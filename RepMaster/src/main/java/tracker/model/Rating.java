/*package tracker.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int id;
    @OneToMany(mappedBy = "rating_")
    private List<RatingComment> allRatings;
    private double rating;
    @OneToOne
    private Exercise exercise;
    @OneToOne
    private Gym gym;
    @OneToOne
    private Workout workout;

    @Getter
    @Setter
    @EqualsAndHashCode(of = "id")
    @Entity
    public class RatingComment{
        @Id
        @GeneratedValue
        private int id;
        String comment;
        int rating;
        @ManyToOne
        private Rating rating_;

        public RatingComment(String comment, int rating){
            this.comment = comment;
            this.rating = rating;
        }

        public RatingComment() {

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
}*/

package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Entity
public class Rating {
    @Id
    @GeneratedValue
    private int id;

    //@OneToMany(mappedBy = "rating_")
    //private List<RatingComment> allRatings;

    private double rating;
    private String comment;

    @ManyToOne
    private Exercise exercise;
    @ManyToOne
    private Gym gym;
    @ManyToOne
    private Workout workout;


    public Rating(double rating, String comment){
        this.rating = rating;
        this.comment = comment;
    }
    /*@Getter
    @Setter
    @EqualsAndHashCode(of = "id")
    @Entity
    public class RatingComment{
        @Id
        @GeneratedValue
        private int id;
        String comment;
        int rating;
        @ManyToOne
        private Rating rating_;

        public RatingComment(String comment, int rating){
            this.comment = comment;
            this.rating = rating;
        }

        public RatingComment() {

        }
    }*/

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

