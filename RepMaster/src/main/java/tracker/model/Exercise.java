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
public class Exercise implements Rateable{
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

    @OneToMany(mappedBy = "exercise")
    private List<Rating> ratings;
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

    @Override
    public void addRating(Rating r) {
        ratings.add(r);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
    }
}
