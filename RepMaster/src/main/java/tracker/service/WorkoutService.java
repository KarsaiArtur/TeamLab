package tracker.service;

import tracker.model.Exercise;
import tracker.model.Workout;
import tracker.repository.WorkoutRepository;

public class WorkoutService {
    private WorkoutRepository workoutRepository;

    public void addExerciseToWorkout(int id, Exercise exercise){
        Workout workout = workoutRepository.findByID(id);
        addMuscleGroupToWorkout(workout, exercise);
        workout.addExercise(exercise);
    }

    public void addMuscleGroupToWorkout(Workout workout, Exercise exercise){
        if(!workout.getMuscleGroups().contains(exercise.getPrimaryMuscleGroup()))
            workout.addMuscleGroup(exercise.getPrimaryMuscleGroup());
    }
}
