package tracker.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.model.*;
import tracker.repository.ExerciseRepository;
import tracker.repository.GymRepository;
import tracker.repository.RatingRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public void saveWorkout(Workout workout){
        workoutRepository.save(workout);
    }

    @Transactional
    public void deleteWorkout(int id){
        Optional<Workout> workout = workoutRepository.findById(id);
        workoutRepository.delete(workout.get());
    }

    public List<Workout> listWorkouts() {
        return workoutRepository.findAll();
    }

    public List<Exercise> listExercises(int id){
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getExercises();
    }

    public List<MuscleGroup> listMuscleGroups(int id){
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getMuscleGroups();
    }

    @Transactional
    public void deleteAll(){
        workoutRepository.deleteAllInBatch();
    }

    @Transactional
    public void addNewExerciseToWorkout(int workoutId, Exercise exercise){
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        exerciseRepository.save(exercise);
        workout.get().addExercise(exercise);
        addMuscleGroupToWorkout(workout.get(), exercise.getPrimaryMuscleGroup());
    }

    @Transactional
    public void addExistingExerciseToWorkout(int workoutId, int exerciseId){
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().addExercise(exercise.get());
        addMuscleGroupToWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
    }

    @Transactional
    public void addMuscleGroupToWorkout(Workout workout, MuscleGroup muscleGroup){
        if(!workout.getMuscleGroups().contains(muscleGroup))
            workout.addMuscleGroup(muscleGroup);
    }

    @Transactional
    public void removeExerciseFromWorkout(int workoutId, int exerciseId){
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().removeExercise(exercise.get());
    }

}
