package tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.model.Exercise;
import tracker.model.Workout;
import tracker.repository.WorkoutRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;

    @Transactional
    public void addExerciseToWorkout(int id, Exercise exercise){
        Optional<Workout> workout = workoutRepository.findById(id);
        addMuscleGroupToWorkout(workout.get(), exercise);
        workout.get().addExercise(exercise);
    }

    public void addMuscleGroupToWorkout(Workout workout, Exercise exercise){
        if(!workout.getMuscleGroups().contains(exercise.getPrimaryMuscleGroup()))
            workout.addMuscleGroup(exercise.getPrimaryMuscleGroup());
    }
}
