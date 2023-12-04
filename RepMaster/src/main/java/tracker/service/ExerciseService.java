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

/**
 * gyakorlatok service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
 */
@RequiredArgsConstructor
@Service
public class ExerciseService implements RateableService{
    /**
     * az edzőtervekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtervekkel
     */
    private final WorkoutRepository workoutRepository;
    /**
     * a gyakorlatokhoz tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő gyakorlatokkal
     */
    private final ExerciseRepository exerciseRepository;
    /**
     * az eremdényekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő eredményekkel
     */
    private final ExerciseResultRepository exerciseResultRepository;
    /**
     * edzőtervek service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final WorkoutService workoutService;
    /**
     * regisztrált felhasználók service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final RegisteredUserService registeredUserService;
    /**
     * eremdények service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final ExerciseResultService exerciseResultService;

    /**
     * egy id alapján visszaadja a keresett gyakorlatot az adatbázisból
     * @param id a keresett gyakorlat id-ja
     * @return a keresett gyakorlat
     */
    public Exercise findExercise(int id) {
        return exerciseRepository.findById(id).isEmpty() ? null : exerciseRepository.findById(id).get();
    }

    /**
     * visszaadja az aktuális felhasználó edzőterveit, amibe lehet gyakorlatot tenni
     * @return aktuális felhasználó edzőtervei
     */
    @Override
    public List<Rateable> getPossibleContainers(){
        List<Rateable> rateables = new ArrayList<>();
        for (Workout workout: workoutService.listUserWorkouts()) {
            rateables.add(workout);
        }
        return rateables;
    }

    /**
     * hozzáad egy létező gyakorlatot egy létező edzőtervhez
     * @param idTo a létező edzőterv id-ja
     * @param id a létező gyakorlat id-ja
     */
    @Override
    public void addRateable(int idTo, int id){
        workoutService.addExistingExerciseToWorkout(idTo, id);
    }

    /**
     * kilistázza egy edzőterv gyakorlatait
     * @param workoutId a keresett edzőterv id-ja
     * @return a gyakorlatok listája
     */
    public List<Exercise> listExercisesByWorkoutId(int workoutId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        return workout.get().getExercises();
    }

    /**
     * elmenti az adott gyakorlathoz tartozó eredményeket
     * @param exercise gyakorlat, aminek az eredményeit elmenti
     */
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

    /**
     * egy az adatbázisban még nem szereplő eredményt hozzáad egy gyakorlathoz
     * @param id a keresett gyakorlat id-ja
     * @param result a hozzáadandó eredmény
     */
    @Transactional
    public void addNewResult(int id, ExerciseResult result){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        exerciseResultRepository.save(result);
        exercise.get().addNewResult(result);
    }

    /**
     * egy már az adatbázisban szereplő eredményt hozzáad egy gyakorlathoz
     * @param id a keresett gyakorlat id-ja
     * @param result_id a hozzáadandó eredmény id-ja
     */
    @Transactional
    public void addExistingResultToExercise(int id, int result_id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Optional<ExerciseResult> result = exerciseResultRepository.findById(result_id);
        exercise.get().addNewResult(result.get());
    }

    /**
     * felsorolja egy gyakorlatról az eredményeket
     * @param id a keresett gyakorlat id-ja
     * @return a keresett eredmények listája
     */
    public List<ExerciseResult> listResults(int id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.get().getExerciseResults();
    }

    /**
     * megkeresi egy gyakorlathoz tartozó legnagyobb súlyt, amit használt
     * @param id a keresett gyakorlat id-ja
     * @return a legnagyobb súly, amit használt
     */
    public double findPR(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        double maxWeight = 0;

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            double curWeight = e.findPrWithinSets();
            maxWeight = (maxWeight < curWeight) ? curWeight : maxWeight;
        }

        return maxWeight;
    }

    /**
     * megkeresi egy gyakorlathoz tartozó legjobb szetten belüli edzési volument
     * @param id a keresett gyakorlat id-ja
     * @return a legjobb szett edzési volumenje
     */
    public Set findMaxVolume(int id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        Set maxVolume = new Set(0, 0);

        for (ExerciseResult e: exercise.get().getExerciseResults()) {
            Set curVolume = e.findMaxVolumeWithinSets();
            maxVolume = (maxVolume.getVolume() < curVolume.getVolume()) ? curVolume : maxVolume;
        }

        return maxVolume;
    }

    /**
     * megkeresi egy gyakorlathoz tartozó legjobb eredmény edzési volumenjét
     * @param id a keresett gyakorlat id-ja
     * @return a legjobb eredmény edzési volumenje
     */
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


    /**
     * megkeresi egy gyakorlathoz tartozó legjobb eredményt (maximális edzés volumen)
     * @param id a keresett gyakorlat id-ja
     * @return a legjobb eredmény
     */
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

    /**
     * egy gyakorlathoz hozzáad másodlagosan edzett izomcsoportot
     * @param id a keresett gyakorlat id-ja
     * @param muscleGroup a hozzáadott izomcsoport
     */
    @Transactional
    public void addSecondaryMuscleGroup(int id, MuscleGroup muscleGroup){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        exercise.get().addSecondaryMuscleGroup(muscleGroup);
    }

    /**
     * megkeres egy gyakorlatot az adatbázisban, majd Rateable-ként visszaadja
     * @param id a keresett gyakorlat id-ja
     * @return a keresett gyakorlat Rateableként
     */
    @Override
    public Rateable findById(int id) {
        return exerciseRepository.findById(id).get();
    }
}
