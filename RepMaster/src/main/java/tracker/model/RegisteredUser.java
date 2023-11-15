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
    @OneToMany(mappedBy = "registeredUser")
    private List<Gym> userGyms;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<Rating> ratings;

    /*????????????????????????????
    @ManyToMany(mappedBy = "registeredUsers")
    private List<Workout> userWorkouts;*/

    public RegisteredUser(String username, String password) {
        this.password = password;
    }

    /*private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }*/

    public void addGym(Gym g) {
        if(userGyms == null) userGyms = new ArrayList<>();
        userGyms.add(g);
    }


    /*public void addWorkoutToUser(Workout w) {
        createWorkoutListIfEmpty();
        userWorkouts.add(w);
    }*/

    /*public void addWorkoutsFromUsersGyms() {
        createWorkoutListIfEmpty();
        for(Gym g: userGyms) {
            List<Workout> workouts = g.getWorkouts();
            userWorkouts.addAll(workouts);
        }
    }*/

    public void addRating(Rating r){
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setRegisteredUser(this);
    }

    public void removeRating(Rating rating) throws Exception{
        ratings.remove(rating);
    }

}
