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

/**
 * az edzőtermek service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
 */
@Builder
@RequiredArgsConstructor
@Service
public class WorkoutService implements RateableService {
    /**
     * az edzőtermekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtermekkel
     */
    private final GymRepository gymRepository;
    /**
     * az edzőtervekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtervekkel
     */
    private final WorkoutRepository workoutRepository;
    /**
     * a gyakorlatokhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő gyakorlatokkal
     */
    private final ExerciseRepository exerciseRepository;
    /**
     * az értékelésekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő értékeléssekkel
     */
    private final RatingRepository ratingRepository;
    /**
     * a regisztrált felhasználókhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált felhasználókkal
     */
    private final RegisteredUserRepository registeredUserRepository;
    /**
     * az edzőtervek service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final GymService gymService;
    /**
     * a regisztrált felhasználó service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
     */
    private final RegisteredUserService registeredUserService;

    /**
     * elment egy edzőtervet az adatbázisba
     * @param workout az elmentett edzőterv
     */
    @Transactional
    public void saveWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    /**
     * kitöröl egy az adatbázisban szereplő edzőtermet. ehhez előbb ki kell törölni az adatbázisból a hozzá tartozó értékeléseket, illetve ki kell venni a jelenlegi edzőteremből
     * @param id a törölt edzőterem id-ja
     */
    @Transactional
    public void deleteWorkout(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        if(workout.get().getRatings() != null) {
            int size = workout.get().getRatings().size();
            for (int i = 0; i < size; i++) {
                registeredUserService.deleteRating(workout.get(), workout.get().getRatings().get(0).getId());
            }
        }
        gymService.removeWorkoutFromGym(TrackerApplication.getInstance().getCurrentGym().getId(), id);
        workoutRepository.delete(workout.get());
    }

    /**
     * kitörli az adott id-jú edzőtervet az adatbázisból
     * @param id edzőterv id-ja
     */
    @Transactional
    public void deleteW(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        workoutRepository.delete(workout.get());
    }

    /**
     * visszaadja az aktuális felhasználó edzőtermeit, amibe lehet edzőtervet tenni
     * @return aktuális felhasználó edzőtermei
     */
    @Override
    public List<Rateable> getPossibleContainers(){
        List<Rateable> rateables = new ArrayList<>();
        for (Gym gym: gymService.listUserGyms()) {
            rateables.add(gym);
        }
        return rateables;
    }

    /**
     * hozzáad egy létező edzőtervet egy létező edzőteremhez
     * @param idTo edzőterem id-ja
     * @param id edzőterv id-ja
     */
    @Override
    public void addRateable(int idTo, int id){
        gymService.addExistingWorkoutToGym(idTo, id);
    }

    /**
     * megkeresi és visszatér az adott id-jú edzőtervvel
     * @param id edzőterv id-ja
     * @return megtalált edzőterv
     */
    public Workout findWorkout(int id) {
        return workoutRepository.findById(id).isEmpty() ? null : workoutRepository.findById(id).get();
    }

    /**
     * kilistázza az összes edzőtervet az adatbázisból
     * @return az edzőtervek listája
     */
    public List<Workout> listWorkouts() {
        return workoutRepository.findAll();
    }

    /**
     * kilistázza az adott edzőterem id-hoz tartozó edzőterveket
     * @param gymId edzőterem id-ja
     * @return edzőtervek listája
     */
    public List<Workout> listWorkoutsByGymId(int gymId) {
        Optional<Gym> gym = gymRepository.findById(gymId);
        return gym.get().getWorkouts();
    }

    /**
     * kilistázza a bejelentkezett felhasználóhoz tartozó edzőterveket
     * @return az edzőtervek listája
     */
    public List<Workout> listUserWorkouts() {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        return registeredUser.get().getUserWorkouts();
    }

    /**
     * kilistázza az adott edzőterv id-hoz tartozó gyakorlatokat
     * @param id edzőterv id-ja
     * @return gyakorlatok listája
     */
    public List<Exercise> listExercises(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getExercises();
    }

    /**
     * kilistázza az adott edzőterv id-hoz tartozó izomcsoportokat
     * @param id edzőterv id-ja
     * @return izomcsoportok listája
     */
    public List<MuscleGroup> listMuscleGroups(int id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.get().getMuscleGroups();
    }

    /**
     * kitörli az összes edzőtervet az adatbázisból
     */
    @Transactional
    public void deleteAll() {
        workoutRepository.deleteAllInBatch();
    }

    /**
     * hozzáadja az adott id-jú edzőtervhez az adott gyakorlatot.
     * Ha az aktuális felhasználó nem egyezik az edzőtervet létrehozóval, akkor duplikálja az edzőtervet, és az újnak az aktuális felhasználó lesz a létrehozója és ebbe tevődik bele az új gyakorlat.
     * @param workoutId edzőterv id-ja, amibe beletesszük
     * @param exercise gyakorlat, amit beleteszünk
     */
    @Transactional
    public void addNewExerciseToWorkout(int workoutId, Exercise exercise) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        exerciseRepository.save(exercise);

        if(workout.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()){
            workout.get().addExercise(exercise);
            addMuscleGroupToWorkout(workout.get(), exercise.getPrimaryMuscleGroup());
        }
        else{
            Workout newWorkout = workoutChanged(workout.get());
            newWorkout.addExercise(exercise);
        }
    }

    /**
     * hozzáadja az adott id-jú edzőtervhez az adott id-jú gyakorlatot.
     * Ha az aktuális felhasználó nem egyezik az edzőtevet létrehozóval, akkor duplikálja az edzőtervet, és az újnak az aktuális felhasználó lesz a létrehozója és ebbe tevődik bele a gyakorlat.
     * @param workoutId edzőterv id-ja, amibe beletesszük
     * @param exerciseId gyakorlat id-ja, amit beleteszünk
     */
    @Transactional
    public void addExistingExerciseToWorkout(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);

        if(workout.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()){
            workout.get().addExercise(exercise.get());
            addMuscleGroupToWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
        }
        else{
            Workout newWorkout = workoutChanged(workout.get());
            newWorkout.addExercise(exercise.get());
        }
    }

    /**
     * kiveszi az adott id-jú edzőtervből az adott id-jú gyakorlatot.
     * Ha az aktuális felhasználó nem egyezik az edzőtervet létrehozóval, akkor duplikálja az edzőtervet, és az újnak az aktuális felhasználó lesz a létrehozója és ebből veszi ki a gyakorlatot.
     * @param workoutId edzőterv id-ja, amiből kiveszünk
     * @param exerciseId gyakorlat id-ja, amit kiveszünk
     */
    @Transactional
    public void removeExerciseFromWorkout(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);

        if(workout.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            workout.get().removeExercise(exercise.get());
            exercise.get().removeWorkout(workout.get());
            removeMuscleGroupFromWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
        }
        else{
            Workout newWorkout = workoutChanged(workout.get());
            newWorkout.removeExercise(exercise.get());
            exercise.get().removeWorkout(newWorkout);
            removeMuscleGroupFromWorkout(newWorkout, exercise.get().getPrimaryMuscleGroup());
        }
    }

    /**
     * duplikálja a megadott edzőtervet, annyi különbséggel, hogy az újnak az aktuális felhasználó lesz a létrehozója
     * @param workout edzőterv, amit duplikál
     * @return új duplikált edzőterv
     */
    @Transactional
    public Workout workoutChanged(Workout workout){
        Workout newWorkout = Workout.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(workout.getName())
                .publiclyAvailable(false)
                .muscleGroups(new ArrayList<>())
                .build();

        gymService.addNewWorkoutToGym(TrackerApplication.getInstance().getCurrentGym().getId(), newWorkout);
        for(Exercise exercises: workout.getExercises()){
            addExistingExerciseToWorkout(newWorkout.getId(), exercises.getId());
        }

        gymService.removeWorkoutFromGym(TrackerApplication.getInstance().getCurrentGym().getId(), workout.getId());

        TrackerApplication.getInstance().setCurrentWorkout(newWorkout);
        return newWorkout;
    }

    /**
     * hozzáadja az adott izomcsoportot az adott edzőtervhez, ha még nincs benne
     * @param workout edzőterv, amihez hozzáad
     * @param muscleGroup izomcsoport, amit hozzáad
     */
    @Transactional
    public void addMuscleGroupToWorkout(Workout workout, MuscleGroup muscleGroup) {
        if (!workout.getMuscleGroups().contains(muscleGroup))
            workout.addMuscleGroup(muscleGroup);
    }

    /**
     * kiveszi az adott izomcsoportot az adott edzőtervből, ha nem tartalmaz olyan gyakorlatot, aminek az a fő izomcsoportja
     * @param workout edzőterv, amiből kivesz
     * @param muscleGroup izomcsoport, amit kivesz
     */
    @Transactional
    public void removeMuscleGroupFromWorkout(Workout workout, MuscleGroup muscleGroup) {
        for (Exercise ex : listExercises(workout.getId()))
            if (ex.getPrimaryMuscleGroup() == muscleGroup)
                return;
        workout.removeMuscleGroup(muscleGroup);
    }

    /**
     * hozzáadja az adott id-jú edzőtervhez az adott gyakorlatot
     * @param workoutId edzőterv id-ja, amibe beletesszük
     * @param exercise gyakorlat, amit beleteszünk
     */
    @Transactional
    public void addNewExerciseToW(int workoutId, Exercise exercise) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        exerciseRepository.save(exercise);
        workout.get().addExercise(exercise);
        addMuscleGroupToWorkout(workout.get(), exercise.getPrimaryMuscleGroup());
    }

    /**
     * hozzáadja az adott id-jú edzőtervhez az adott id-jú gyakorlatot
     * @param workoutId edzőterv id-ja, amibe beletesszük
     * @param exerciseId gyakorlat id-ja, amit beleteszünk
     */
    @Transactional
    public void addExistingExerciseToW(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().addExercise(exercise.get());
        addMuscleGroupToWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
    }

    /**
     * kiveszi az adott id-jú edzőtervből az adott id-jú gyakorlatot
     * @param workoutId edzőterv id-ja, amiből kivesszük
     * @param exerciseId gyakorlat id-ja, amit kiveszünk
     */
    @Transactional
    public void removeExerciseFromW(int workoutId, int exerciseId) {
        Optional<Workout> workout = workoutRepository.findById(workoutId);
        Optional<Exercise> exercise = exerciseRepository.findById(exerciseId);
        workout.get().removeExercise(exercise.get());
        exercise.get().removeWorkout(workout.get());
        removeMuscleGroupFromWorkout(workout.get(), exercise.get().getPrimaryMuscleGroup());
    }

    /**
     * megkeres egy edzőtervet az adatbázisban, majd Rateable-ként visszaadja
     * @param id a keresett edzőterv id-ja
     * @return a keresett edzőterv Rateableként
     */
    @Override
    public Rateable findById(int id) {
        return workoutRepository.findById(id).get();
    }
}
