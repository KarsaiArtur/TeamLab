package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.thymeleaf.util.StringUtils;
import tracker.web.RateableDetailTLController;

import java.util.ArrayList;
import java.util.List;

/**
 * Gyakorlat osztály, rateableből származik le
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exercise extends Rateable{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int set_count;
    private int repetition_count;
    private boolean isCompound;
    private boolean publiclyAvailable = true;

    /**
     * fő izomcsoport, amit edz
     */
    @Enumerated(EnumType.STRING)
    private MuscleGroup primaryMuscleGroup;

    /**
     * másodlagos izomcsoportok, amiket edz
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="secondary_muscle_groups")
    @Column(name="muscle_group")
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> secondaryMuscleGroups;

    /**
     * értékelések a gyakorlatról
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exercise")
    private List<Rating> ratings;
    /**
     * gyakorlat eredményei
     */
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exercise")
    private List<ExerciseResult> exerciseResults;

    /**
     * edzőtervek, amik tartalmazzák a gyakorlatot
     */
    @ManyToMany(mappedBy = "exercises", fetch = FetchType.EAGER)
    private List<Workout> workouts;
    /**
     * gyakorlatot létrehozó felhasználó
     */
    @ManyToOne
    private RegisteredUser owner;

    public Exercise(int set_count) {
        this.set_count = set_count;
    }

    public void addSecondaryMuscleGroup(MuscleGroup muscleGroup){
        if(secondaryMuscleGroups == null){
            secondaryMuscleGroups = new ArrayList<>();
        }
        secondaryMuscleGroups.add(muscleGroup);
    }

    /**
     * hozzá lett adva egy edzőtervhez a gyakorlat. ez a függvény hozzáadja a listájához ezt az edzőtervet
     * @param workout edzőterv, amit hozzáadunk
     */
    public void addWorkout(Workout workout){
        if(workouts == null){
            workouts = new ArrayList<>();
        }
        workouts.add(workout);
    }

    public void removeWorkout(Workout workout){
        workouts.remove(workout);
    }

    public void addNewResult(ExerciseResult exerciseResult){
        if(exerciseResults == null){
            exerciseResults = new ArrayList<>();
        }
        exerciseResult.setExercise(this);
        exerciseResults.add(exerciseResult);
    }

    public void removeResult(ExerciseResult r){
        exerciseResults.remove(r);
    }

    /**
     * értékelés hozzáadása a gyakorlatról
     * @param r értékelés, amit hozzáadunk
     */
    @Override
    public void addRating(Rating r) {
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setExercise(this);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setExercise(null);
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
        return name+" ("+primaryMuscleGroup.toString().replace("_", " ")+") "+ (rating==0.0 ? "Not rated": "AVG rating "+rating+"/5.0");
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
        details.add(new RateableDetailTLController.Details("Exercise name: ", name));
        details.add(new RateableDetailTLController.Details("Set count: ", set_count+""));
        details.add(new RateableDetailTLController.Details("Repetition count: ", repetition_count+""));
        details.add(new RateableDetailTLController.Details(isCompound ? "Compound " : "Not compound", ""));
        details.add(new RateableDetailTLController.Details("Primary Muscle group", primaryMuscleGroup.toString()));
        details.add(new RateableDetailTLController.Details("Secoundary Muscle group", secondaryMuscleGroups.toString()));
        details.add(new RateableDetailTLController.Details("Average rating: ", s_rating+  StringUtils.repeat("⭐", (int)rating)));
        return details;
    }
}
