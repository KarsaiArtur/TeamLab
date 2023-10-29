package tracker.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Workout {
    private int id;
    private String name;
    private Rating rating;
    private List<Exercise> exercises;
    private List<MuscleGroup> muscleGroups;

    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();

        exercises.add(e);
    }

    public void addMuscleGroup(MuscleGroup m){
        if(muscleGroups == null)
            muscleGroups = new ArrayList<>();

        muscleGroups.add(m);
    }

    public void addRating(int r, String comment) {
        rating.addRating(r, comment);
    }
}
