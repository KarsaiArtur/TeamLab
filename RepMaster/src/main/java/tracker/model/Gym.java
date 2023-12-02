package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.thymeleaf.util.StringUtils;
import tracker.web.RateableDetailTLController;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gym")
    private List<Rating> ratings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "split_id", referencedColumnName = "id")
    private Split split;

    @Enumerated(EnumType.STRING)
    private Equipment howEquipped;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "gym_workout_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<Workout> workouts;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "registered_user_gym_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "registeredUser_id")
    )
    private List<RegisteredUser> registeredUsers;

    @ManyToOne
    private RegisteredUser owner;

    public void addWorkout(Workout w){
        if(workouts == null)
            workouts = new ArrayList<>();
        workouts.add(w);
    }

    public void addRegisteredUser(RegisteredUser rU){
        if(registeredUsers == null)
            registeredUsers = new ArrayList<>();
        registeredUsers.add(rU);
    }

    @Override
    public void addRating(Rating r) {
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setGym(this);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setGym(null);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }

    public void removeWorkout(Workout w){
        workouts.remove(w);
    }

    @Override
    public String toString(){
        return name +" "+split.getName().toString().replace("_", " ");
    }

    @Override
    public String getName(){
        return name;
    }

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
