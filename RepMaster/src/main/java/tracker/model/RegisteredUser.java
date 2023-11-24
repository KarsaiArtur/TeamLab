package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RegisteredUser implements User{
    @Id
    @GeneratedValue
    private int id;
    private String userName;
    private String password;
    @ManyToMany(mappedBy = "registeredUsers", fetch = FetchType.EAGER)
    private List<Gym> userGyms;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<Rating> ratings;
    @ManyToMany(mappedBy = "registeredUsers", fetch = FetchType.EAGER)
    private List<Workout> userWorkouts;


    private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }

    public void addGym(Gym g) {
        if(userGyms == null) userGyms = new ArrayList<>();
        g.addRegisteredUser(this);
        userGyms.add(g);
    }


    public void addWorkoutToUser(Workout w) {
        createWorkoutListIfEmpty();
        userWorkouts.add(w);
    }

    public void addWorkoutsFromUsersGyms() {
        createWorkoutListIfEmpty();
        for(Gym g: userGyms) {
            List<Workout> workouts = g.getWorkouts();
            userWorkouts.addAll(workouts);
        }
    }

    public void addRating(Rateable rateable, Rating r){
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setRegisteredUser(this);
        rateable.addRating(r);
    }

    public void removeRating(Rating rating) throws Exception{
        ratings.remove(rating);
    }

}
