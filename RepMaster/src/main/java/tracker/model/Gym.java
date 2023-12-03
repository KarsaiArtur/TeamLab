package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.thymeleaf.util.StringUtils;
import tracker.web.RateableDetailTLController;

import java.util.ArrayList;
import java.util.List;

/**
 * Edzőterem osztály, rateableből származik le
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Gym extends Rateable{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String location;
    private boolean publiclyAvailable = true;

    /**
     * értékelések
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gym")
    private List<Rating> ratings;

    /**
     * edzőteremhez tartozó split(izomcsoport felbontás)
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "split_id", referencedColumnName = "id")
    private Split split;

    /**
     * mennyire felszerelt az edzőterem
     */
    @Enumerated(EnumType.STRING)
    private Equipment howEquipped;

    /**
     * az edzőteremhez tartozó edzőtervek
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "gym_workout_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<Workout> workouts;

    /**
     * felhasználók, akik használják az edzőtervet
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "registered_user_gym_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "registeredUser_id")
    )
    private List<RegisteredUser> registeredUsers;

    /**
     * az edzőtermet létrehozó felhasználó
     */
    @ManyToOne
    private RegisteredUser owner;

    public void addWorkout(Workout w){
        if(workouts == null)
            workouts = new ArrayList<>();
        workouts.add(w);
    }

    /**
     * felhasználó hozzáadása, akik használják ezt az edzőtermet
     * @param rU a hozzáadott felhasználó
     */
    public void addRegisteredUser(RegisteredUser rU){
        if(registeredUsers == null)
            registeredUsers = new ArrayList<>();
        registeredUsers.add(rU);
    }

    /**
     * értékelés hozzáadása
     * @param r értékelés, amit hozzáad
     */
    @Override
    public void addRating(Rating r) {
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setGym(this);
    }

    /**
     * egy értékelés eltávolítása az edzőtermhez tartozó értékelések közül
     * @param r az eltávolítandó értékelés
     */
    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setGym(null);
    }

    public void removeRegisteredUser(RegisteredUser r) {
        registeredUsers.remove(r);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }

    /**
     * az edzőteremből eltávolít egy adott edzőtervet
     * @param w az eltávolított edzőterv
     */
    public void removeWorkout(Workout w){
        workouts.remove(w);
    }

    /**
     * stringgé alakítja az osztály tartalmát
     * @return az átalakított string
     */
    @Override
    public String toString(){
        double rating = Rating.calculateRating(this);
        return name +" ("+split.getName().toString().replace("_", " ")+" "+split.getNumberOfDays()+" days) "+ (rating==0.0 ? "Not rated": "AVG rating "+rating+"/5.0");
    }

    @Override
    public String getName(){
        return name;
    }

    /**
     * stringgé alakítja az osztály tartalmát, majd beleteszi Details objektumokba, ahol különválasztja a tulajdonságot és a tulajdonság értéket
     * @return az átalakított stringeket tároló Details lista
     */
    @Override
    public List<RateableDetailTLController.Details> details(){
        double rating = Rating.calculateRating(this);
        String s_rating = (rating == 0.0) ? "Not rated yet" : rating+"";
        List<RateableDetailTLController.Details> details = new ArrayList<>();
        details.add(new RateableDetailTLController.Details("Gym name: ", name));
        details.add(new RateableDetailTLController.Details(howEquipped.toString(), ""));
        details.add(new RateableDetailTLController.Details("Split: ",split.getName()+" - "+split.getNumberOfDays()+" days"));
        details.add(new RateableDetailTLController.Details("Location: ", location));
        details.add(new RateableDetailTLController.Details("Average rating: ", s_rating+  StringUtils.repeat("⭐", (int)rating)));
        details.add(new RateableDetailTLController.Details("Users: ",""+registeredUsers.size()));
        return details;
    }
}
