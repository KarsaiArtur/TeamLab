package tracker.model;

import java.util.List;
import java.util.Objects;

public interface Search {

    public List<Workout> searchWorkoutBySplit(Object o);
    /*public List<Workout> searchWorkoutByMuscleGroup(MuscleGroup muscleGroup);
    public List<Workout> searchWorkoutByRating(int rating);*/

    public List<Exercise> searchExerciseBySplit(Object o);
    /*public List<Exercise> searchExerciseByMuscleGroup(MuscleGroup muscleGroup);
    public List<Exercise> searchExerciseByRating(int rating);*/
}
