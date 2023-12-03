package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.thymeleaf.util.StringUtils;
import tracker.web.RateableDetailTLController;

import java.util.ArrayList;
import java.util.List;

/**
 * Edzőterv osztály, rateableből származik le
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Workout extends Rateable {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private boolean publiclyAvailable = true;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workout")
    private List<Rating> ratings;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "workout_exercise_connection",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="muscle_groups")
    @Column(name="muscle_group")
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> muscleGroups;

    @ManyToMany(mappedBy = "workouts", fetch = FetchType.EAGER)
    private List<Gym> gyms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "registered_user_workout_connection",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "registeredUser_id")
    )
    private List<RegisteredUser> registeredUsers;

    @ManyToOne
    private RegisteredUser owner;

    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();
        exercises.add(e);
        e.addWorkout(this);
    }

    public void removeExercise(Exercise e){
        exercises.remove(e);
    }

    public void addMuscleGroup(MuscleGroup m){
        if(muscleGroups == null)
            muscleGroups = new ArrayList<>();

        muscleGroups.add(m);
    }

    public void removeMuscleGroup(MuscleGroup m){
        muscleGroups.remove(m);
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
        r.setWorkout(this);
    }
    /**
     * felhasználó hozzáadása, akik használják ezt az edzőtervet
     * @param rU a hozzáadott felhasználó
     */
    public void addRegisteredUser(RegisteredUser rU){
        if(registeredUsers == null)
            registeredUsers = new ArrayList<>();

        registeredUsers.add(rU);
    }
    /**
     * egy értékelés eltávolítása az edzőtervhez tartozó értékelések közül
     * @param r az eltávolítandó értékelés
     */
    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setWorkout(null);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }
    /**
     * stringgé alakítja az osztály tartalmát
     * @return az átalakított string
     */
    @Override
    public String toString(){
        double rating = Rating.calculateRating(this);
        return name + (rating==0.0 ? " Not rated": " AVG rating "+rating+"/5.0");
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
        details.add(new RateableDetailTLController.Details("Workout name: ", name));
        details.add(new RateableDetailTLController.Details("Worked musclegroups: ", muscleGroups.toString()));
        details.add(new RateableDetailTLController.Details("Average rating: ", s_rating+  StringUtils.repeat("⭐", (int)rating)));
        details.add(new RateableDetailTLController.Details("Users: ",""+registeredUsers.size()));
        return details;
    }
}
