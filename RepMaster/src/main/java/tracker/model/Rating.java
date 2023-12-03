package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.thymeleaf.util.StringUtils;

/**
 * értékelés osztály
 */
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

    /**
     * értékelés értéke
     */
    private double rating;
    /**
     * értékeléshez tartozó komment
     */
    private String comment;

    /**
     * gyakorlat, amihez az értékelés tartozik
     */
    @ManyToOne
    private Exercise exercise;
    /**
     * edzőterv, amihez az értékelés tartozik
     */
    @ManyToOne
    private Workout workout;
    /**
     * edzőterem, amihez az értékelés tartozik
     */
    @ManyToOne
    private Gym gym;
    /**
     * felhasználó, aki az értékelést csinálta
     */
    @ManyToOne
    private RegisteredUser registeredUser;

    /**
     * értékelés értékének kiszámítása
     * @param r értékelhető osztály
     * @return kiszámított érték
     */
    public static double calculateRating(Rateable r) {
        int sum = 0;
        int ratingsCnt = 0;
        for(Rating currentRating : r.getRatings()) {
            ratingsCnt++;
            sum += currentRating.getRating();
        }
        if(ratingsCnt == 0) return 0;
        return (sum / (double)ratingsCnt);
    }

    /**
     * stringgé alakítja az osztály tartalmát
     * @return az átalakított string
     */
    @Override
    public String toString(){
        return comment+" "+StringUtils.repeat("⭐", (int)rating)+ " by "+registeredUser.getUserName();
    }
}

