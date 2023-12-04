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
public class RegisteredUser{
    @Id
    @GeneratedValue
    private int id;
    /**
     * felhasználónév
     */
    private String userName;
    /**
     * jelszó
     */
    private String password;
    /**
     * felhasználóhoz tartozó edzőtermek
     */
    @ManyToMany(mappedBy = "registeredUsers", fetch = FetchType.EAGER)
    private List<Gym> userGyms;
    /**
     * felhasználóhoz tartozó edzőtervek
     */
    @ManyToMany(mappedBy = "registeredUsers", fetch = FetchType.EAGER)
    private List<Workout> userWorkouts;
    /**
     * felhasználóhoz tartozó értékelések
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<Rating> ratings;
    /**
     * felhasználóhoz tartozó gyakorlat eredmények
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "registeredUser")
    private List<ExerciseResult> exerciseResults;
    /**
     * edzőterv, amit a felhasználó hozott létre
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Workout> ownedWorkout;
    /**
     * edzőterem, amit a felhasználó hozott létre
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<Gym> ownedGym;

    /**
     * létrehozza az edzőterv listát ha üres
     */
    private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }

    /**
     * hozzáad edzőtermet
     * @param gym edzőterem, amit hozzáad
     */
    public void addGym(Gym gym) {
        if(userGyms == null) userGyms = new ArrayList<>();
        gym.addRegisteredUser(this);
        userGyms.add(gym);
    }

    /**
     * kivesz edzőtermet
     * @param gym edzőterem, amit kivesz
     */
    public void removeGym(Gym gym) {
        gym.removeRegisteredUser(this);
        userGyms.remove(gym);
    }

    /**
     * hozzáad edzőtervet
     * @param w edzőterv, amit hozzáad
     */
    public void addWorkout(Workout w) {
        createWorkoutListIfEmpty();
        w.addRegisteredUser(this);
        userWorkouts.add(w);
    }

    /**
     * kivesz edzőtermet
     * @param w edzőterem, amit kivesz
     */
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
