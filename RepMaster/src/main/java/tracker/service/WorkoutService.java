package tracker.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Builder
@RequiredArgsConstructor
@Service
public class WorkoutService implements RateableService {
    private final GymRepository gymRepository;
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final RatingRepository ratingRepository;
    private final GymService gymService;
    private final RegisteredUserRepository registeredUserRepository;

    @Transactional
    public void saveWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    @Transactional
    public void deleteWorkout(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        workoutRepository.delete(workout.get());
    }

    public List<Rateable> getPossibleContainers(){
        List<Rateable> rateables = new ArrayList<>();
        for (Gym gym: gymService.listUserGyms()) {
            rateables.add(gym);
        }
        return rateables;
    }

    public void addRateable(int idTo, int id){
        gymService.addExistingWorkoutToGym(idTo, id);
    }

    public Workout findWorkout(int id) {
        return workoutRepository.findById(id).isEmpty() ? null : workoutRepository.findById(id).get();
    }

    public List<Workout> listWorkouts() {
        return workoutRepository.findAll();
    }

    public List<Workout> listWorkoutsByGymId(int gymId) {
        Optional<Gym> gym = gymRepository.findById(gymId);
        return gym.get().getWorkouts();
    }

    public List<Workout> listUserWorkouts() {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        return registeredUser.get().getUserWorkouts();
    }

    public List<Exercise> listExercises(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getExercises();
    }

    public List<MuscleGroup> listMuscleGroups(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getMuscleGroups();
    }

    @Transactional
    public void deleteAll() {
        workoutRepository.deleteAllInBatch();
    }

    @Transactional
    public void addNewExerciseToWorkout(int workoutId, Exercise exercise) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        exerciseRepository.save(exercise);
        workout.get().addExercise(exercise);
        addMuscleGroupToWorkout(workout.get(), exercise.getPrimaryMuscleGroup());
    }

    @Transactional
    public void addExistingExerciseToWorkout(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().addExercise(exercise.get());
        addMuscleGroupToWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
    }

    @Transactional
    public void removeExerciseFromWorkout(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().removeExercise(exercise.get());
        exercise.get().removeWorkout(workout.get());
        removeMuscleGroupFromWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
    }

    @Transactional
    public void addMuscleGroupToWorkout(Workout workout, MuscleGroup muscleGroup) {
        if (!workout.getMuscleGroups().contains(muscleGroup))
            workout.addMuscleGroup(muscleGroup);
    }

    @Transactional
    public void removeMuscleGroupFromWorkout(Workout workout, MuscleGroup muscleGroup) {
        for (Exercise ex : listExercises(workout.getId()))
            if (ex.getPrimaryMuscleGroup() == muscleGroup)
                return;
        workout.removeMuscleGroup(muscleGroup);
    }

    @Override
    public Rateable findById(int id) {
        return workoutRepository.findById(id).get();
    }

}
