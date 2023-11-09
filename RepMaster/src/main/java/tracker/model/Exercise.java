package tracker.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exercise {
    @Id
    @GeneratedValue
    private int id;

    //nem kéne ide egy string name?

    private String name;
    private int set_count;
    private int repetition_count;
    private boolean isCompound;
    @ManyToOne
    private MuscleGroup primaryMuscleGroup;

    @OneToMany(mappedBy = "exercise")
    private List<Rating> ratings;

    @ManyToMany
    @JoinTable(
            name = "secondary_musclegroup_exercise_connection",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_group_id")
    )
    private List<MuscleGroup> secondaryMuscleGroups;
    @OneToMany(mappedBy = "exercise")
    private List<ExerciseResult> exerciseResults;
    @ManyToOne
    private Workout workout;

    public Exercise(int set_count) {
        this.set_count = set_count;
    }

    public void addSecondaryMuscleGroup(MuscleGroup muscleGroup){
        if(secondaryMuscleGroups == null){
            secondaryMuscleGroups = new ArrayList<>();
        }
        secondaryMuscleGroups.add(muscleGroup);
    }

    public void addNewResult(ExerciseResult exerciseResult){
        if(exerciseResults == null){
            exerciseResults = new ArrayList<>();
        }
        exerciseResult.setExercise(this);
        exerciseResults.add(exerciseResult);
    }

    public void addRating(double r, String comment) {
        ratings.add(new Rating(r, comment));
    }
}
