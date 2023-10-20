package tracker.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Exercise {
    private int id;
    private int set_count;
    private int repetition_count;
    private MuscleGroup primaryMuscleGroup;
    private List<MuscleGroup> secondaryMuscleGroup;
    private List<ExerciseResult> exerciseResults;

    public Exercise(int set_count) {
        this.set_count = set_count;
    }

    public void addSecondaryMuscleGroup(MuscleGroup muscleGroup){
        if(secondaryMuscleGroup == null){
            secondaryMuscleGroup = new ArrayList<>();
        }
        secondaryMuscleGroup.add(muscleGroup);
    }

    public void addNewResult(ExerciseResult exerciseResult){
        if(exerciseResults == null){
            exerciseResults = new ArrayList<>();
        }
        exerciseResults.add(exerciseResult);
    }
}
