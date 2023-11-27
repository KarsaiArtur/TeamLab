package tracker.model;

import jakarta.persistence.*;
import lombok.*;
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
public class Exercise extends Rateable{
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private int set_count;
    private int repetition_count;
    private boolean isCompound;

    @Enumerated(EnumType.STRING)
    private MuscleGroup primaryMuscleGroup;


    @ElementCollection
    @CollectionTable(name="secondary_muscle_groups")
    @Column(name="muscle_group")
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> secondaryMuscleGroups;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exercise")
    private List<Rating> ratings;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "exercise")
    private List<ExerciseResult> exerciseResults;


    @ManyToMany(mappedBy = "exercises", fetch = FetchType.EAGER)
    private List<Workout> workouts;

    public Exercise(int set_count) {
        this.set_count = set_count;
    }

    public void addSecondaryMuscleGroup(MuscleGroup muscleGroup){
        if(secondaryMuscleGroups == null){
            secondaryMuscleGroups = new ArrayList<>();
        }
        secondaryMuscleGroups.add(muscleGroup);
    }

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

    @Override
    public String toString(){
        return name+" "+primaryMuscleGroup.toString().replace("_", " ");
    }

    @Override
    public String getName(){
        return name;
    }


    @Override
    public List<RateableDetailTLController.Details> details(){
        double rating = Rating.calculateRating(this);
        List<RateableDetailTLController.Details> details = new ArrayList<>();
        return details;
    }
}
