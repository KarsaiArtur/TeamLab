package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * regisztrált felhasználó osztály
 */
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
    @ManyToMany(mappedBy = "registeredUsers", fetch = FetchType.EAGER)
    private List<Workout> userWorkouts;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<Rating> ratings;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<ExerciseResult> exerciseResults;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Workout> ownedWorkout;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Gym> ownedGym;

    private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }

    public void addGym(Gym gym) {
        if(userGyms == null) userGyms = new ArrayList<>();
        gym.addRegisteredUser(this);
        userGyms.add(gym);
    }

    public void removeGym(Gym gym) {
        gym.removeRegisteredUser(this);
        userGyms.remove(gym);
    }

    public void addExerciseResult(ExerciseResult eR) {
        if(exerciseResults == null) exerciseResults = new ArrayList<>();
        eR.setRegisteredUser(this);
        exerciseResults.add(eR);
    }

    public void addWorkout(Workout w) {
        createWorkoutListIfEmpty();
        w.addRegisteredUser(this);
        userWorkouts.add(w);
    }

    public void removeWorkout(Workout w) {
        userWorkouts.remove(w);
    }

    /**
     * hozzáad értékelést
     * @param rateable amihez hozzáadjuk
     * @param r értékelés, amit hozzáadunk
     */
    public void addRating(Rateable rateable, Rating r){
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setRegisteredUser(this);
        rateable.addRating(r);
    }

    public String getName(){
        return userName;
    }
}
