package tracker.model;

import java.util.List;

public interface Search {

    public List<Workout> searchWorkoutBySplit(Gym gym);
    public List<Workout> searchWorkoutByMuscleGroup(MuscleGroup muscleGroup);
    public List<Workout> searchWorkoutByRating(int rating);

    public List<Exercise> searchExerciseBySplit(Gym gym);
    public List<Exercise> searchExerciseByMuscleGroup(MuscleGroup muscleGroup);
    public List<Exercise> searchExerciseByRating(int rating);
}
