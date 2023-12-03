package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.ExerciseRepository;
import tracker.repository.ExerciseResultRepository;
import tracker.repository.WorkoutRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExerciseService implements RateableService{
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseResultRepository exerciseResultRepository;
    private final WorkoutService workoutService;
    private final RegisteredUserService registeredUserService;
    private final ExerciseResultService exerciseResultService;

    public Exercise findExercise(int id) {
        return exerciseRepository.findById(id).isEmpty() ? null : exerciseRepository.findById(id).get();
    }

    public List<Rateable> getPossibleContainers(){
        List<Rateable> rateables = new ArrayList<>();
        for (Workout workout: workoutService.listUserWorkouts()) {
            rateables.add(workout);
        }
        return rateables;
    }

    public void addRateable(int idTo, int id){
        workoutService.addExistingExerciseToWorkout(idTo, id);
    }

    public List<Exercise> listExercises() {
        return exerciseRepository.findAll();
    }

    public List<Exercise> listExercisesByWorkoutId(int workoutId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        return workout.get().getExercises();
    }

    @Transactional
    public void saveExerciseResultInExercise(Exercise exercise) {
        List<Exercise> exercises = exerciseRepository.findByName(exercise.getName());
        exercises.forEach(e -> {
            e.getExerciseResults().forEach(r -> exerciseResultRepository.save(r));
        });
    }

    @Transactional
    public void saveExercise(String name){
        List<Exercise> exercises = exerciseRepository.findByName(name);
        for(Exercise e: exercises)
            exerciseRepository.save(e);
    }

    @Transactional
    public Exercise saveExercise(Exercise ex){
        return exerciseRepository.save(ex);
    }

    @Transactional
    public void deleteExercise(int id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if(exercise.get().getRatings() != null) {
            int size = exercise.get().getRatings().size();
            for (int i = 0; i < size; i++) {
                registeredUserService.deleteRating(exercise.get(), exercise.get().getRatings().get(0).getId());
            }
        }
        if(exercise.get().getExerciseResults() != null) {
            int size = exercise.get().getExerciseResults().size();
            for (int i = 0; i < size; i++) {
                exerciseResultService.deleteResult(exercise.get().getExerciseResults().get(0).getId());
            }
        }
        workoutService.removeExerciseFromWorkout(TrackerApplication.getInstance().getCurrentWorkout().getId(), id);
        exerciseRepository.delete(exercise.get());
    }

    @Transactional
    public void deleteAll(){
        exerciseResultRepository.deleteAllInBatch();
        exerciseRepository.deleteAllInBatch();
    }

    public List<ExerciseResult> listExerciseResults(String name){
        List<Exercise> exercise = exerciseRepository.findByName(name);
        return exercise.get(0).getExerciseResults();
    }

    @Transactional
    public void addNewResults(String id, List<Set> sets) {
        List<Exercise> exercise = exerciseRepository.findByName(id);
        ExerciseResult newResult = ExerciseResult.builder().build();

        for(int i = 0; i < exercise.get(0).getSet_count(); i++){
            newResult.addSet(sets.get(i));
        }
        newResult.setDate(LocalDate.now());
        exercise.get(0).addNewResult(newResult);
    }

    @Transactional
    public void addNewResult(int id, ExerciseResult result){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        exerciseResultRepository.save(result);
        exercise.get().addNewResult(result);
    }

    @Transactional
    public void addExistingResultToExercise(int id, int result_id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Optional<ExerciseResult> result = exerciseResultRepository.findById(result_id);
        exercise.get().addNewResult(result.get());
    }

    @Transactional
    public void removeResultFromExercise(int id, int result_id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Optional<ExerciseResult> result = exerciseResultRepository.findById(result_id);
        exercise.get().removeResult(result.get());
    }

    public List<ExerciseResult> listResults(int id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.get().getExerciseResults();
    }

    public double findPR(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        double maxWeight = 0;

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            double curWeight = e.findPrWithinSets();
            maxWeight = (maxWeight < curWeight) ? curWeight : maxWeight;
        }

        return maxWeight;
    }

    public Set findMaxVolume(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Set maxVolume = new Set(0, 0);

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            Set curVolume = e.findMaxVolumeWithinSets();
            maxVolume = (maxVolume.getVolume() < curVolume.getVolume()) ? curVolume : maxVolume;
        }

        return maxVolume;
    }

    public double findMaxTotalVolume(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        double max = 0;

        for(ExerciseResult e: exercise.get().getExerciseResults()) {
            e.calculateTotalVolume();
            double current = e.getTotalVolume();
            max = Math.max(current, max);
        }
        return max;
    }

    public ExerciseResult findMaxTotalVolumeExerciseResult(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        double max = 0;
        ExerciseResult maxResult = new ExerciseResult();

        for(ExerciseResult e: exercise.get().getExerciseResults()) {
            e.calculateTotalVolume();
            double current = e.getTotalVolume();
            if(current > max) {
                max = current;
                maxResult = e;
            }

        }
        return maxResult;
    }

    @Transactional
    public void addSecondaryMuscleGroup(int id, MuscleGroup muscleGroup){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        exercise.get().addSecondaryMuscleGroup(muscleGroup);
    }

    @Override
    public Rateable findById(int id) {
        return exerciseRepository.findById(id).get();
    }
}
