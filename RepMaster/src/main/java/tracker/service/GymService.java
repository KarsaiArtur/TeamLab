package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.GymRepository;
import tracker.repository.RegisteredUserRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

/**
 * az edzőtervek service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ. megvalósítja a RateableService interfészt
 */
@RequiredArgsConstructor
@Service
public class GymService  implements RateableService{
    /**
     * az edzőtermekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtermekkel
     */
    private final GymRepository gymRepository;
    /**
     * az edzőtervekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtervekkel
     */
    private final WorkoutRepository workoutRepository;
    /**
     * a regisztrált felhasználókhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált felhasználókkal
     */
    private final RegisteredUserRepository registeredUserRepository;
    /**
     * az regisztrált felhasználók service, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ.
     */
    private final RegisteredUserService registeredUserService;

    /**
     * elment egy edzőtermet az adatbázisba
     * @param gym az elmentett edzőterem
     * @return az elmentett edzőterem
     */
    @Transactional
    public Gym saveGym(Gym gym){
        return gymRepository.save(gym);
    }

    /**
     * kitöröl egy az adatbázisban szereplő edzőtermet. ehhez előbb ki kell törölni az adatbázisból a hozzá tartozó értékeléseket, illetve le kell választani a felhasználótól, aki használta
     * @param id a törölt edzőterem id-ja
     */
    @Transactional
    public void deleteGym(int id){
        Optional<Gym> gym = gymRepository.findById(id);
        if(gym.get().getRatings() != null) {
            int size = gym.get().getRatings().size();
            for (int i = 0; i < size; i++) {
                registeredUserService.deleteRating(gym.get(), gym.get().getRatings().get(0).getId());
            }
        }
        registeredUserService.removeGymFromUser(id);
        gymRepository.delete(gym.get());
    }

    /**
     * nem csinál semmit, mert edzőtermet mindig csak az aktuális felhasználóhoz lehet hozzáadni
     * @return null, mert semmilyen Rateable-höz nem lehet hozzáadni
     */
    @Override
    public List<Rateable> getPossibleContainers(){
        return null;
    }

    /**
     * hozzáad egy létező edzőtermet egy létező felhasználóhoz
     * @param idTo a létező edzőterem id-ja
     * @param id a létező felhasználó id-ja
     */
    @Override
    public void addRateable(int idTo, int id){
        registeredUserService.addExistingGymToUser(id);
    }

    /**
     * megkeres egy edzőtermet id alapján az adatbázisban
     * @param id a keresett edzőterem id-ja
     * @return a keresett edzőterem (null ha nincs az adatbázisban)
     */
    public Gym findGym(int id) {
        return gymRepository.findById(id).isEmpty() ? null : gymRepository.findById(id).get();
    }

    /**
     * kilistázza az összes edzőtermet az adatbázisból
     * @return az edzőtermek listája
     */
    public List<Gym> listGyms() {
        return gymRepository.findAll();
    }

    /**
     * kilistázza a bejelentkezett felhasználóhoz tartozó edzőtermeket
     * @return az edzőtermek listája
     */
    public List<Gym> listUserGyms(){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        return registeredUser.get().getUserGyms();
    }


    /**
     * hozzáadja az adott id-jú edzőteremhez ad adott edzőtervet.
     * Ha az aktuális felhasználó nem egyezik az edzőtermet létrehozóval, akkor duplikálja az edzőtermet, és az újnak az aktuális felhasználó lesz a létrehozója és ebbe tevődik bele az új edzőterv.
     * @param id edzőterem id-ja, amibe beletesszük
     * @param workout edzőterv, amit beleteszünk
     */
    @Transactional
    public void addNewWorkoutToGym(int id, Workout workout){
        Optional<Gym> gym = gymRepository.findById(id);
        workoutRepository.save(workout);

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().addWorkout(workout);
            registeredUserService.addExistingWorkoutToUser(workout.getId());
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.addWorkout(workout);
            registeredUserService.addExistingWorkoutToUser(workout.getId());
        }
    }

    /**
     * hozzáadja az adott id-jú edzőteremhez ad adott id-jú edzőtervet.
     * Ha az aktuális felhasználó nem egyezik az edzőtermet létrehozóval, akkor duplikálja az edzőtermet, és az újnak az aktuális felhasználó lesz a létrehozója és ebbe tevődik bele az edzőterv.
     * @param id edzőterem id-ja, amibe beletesszük
     * @param workout_id edzőterv id-ja, amit beleteszünk
     */
    @Transactional
    public void addExistingWorkoutToGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().addWorkout(workout.get());
            registeredUserService.addExistingWorkoutToUser(workout_id);
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.addWorkout(workout.get());
            registeredUserService.addExistingWorkoutToUser(workout_id);
        }
    }

    /**
     * kiveszi az adott id-jú edzőteremből ad adott id-jú edzőtervet.
     * Ha az aktuális felhasználó nem egyezik az edzőtermet létrehozóval, akkor duplikálja az edzőtermet, és az újnak az aktuális felhasználó lesz a létrehozója és ebből veszi ki az edzőtervet.
     * @param id edzőterem id-ja, amiből kiveszünk
     * @param workout_id edzőterv id-ja, amit kiveszünk
     */
    @Transactional
    public void removeWorkoutFromGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().removeWorkout(workout.get());
            registeredUserService.removeWorkoutFromUser(workout_id);
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.removeWorkout(workout.get());
            registeredUserService.removeWorkoutFromUser(workout_id);
        }
    }

    /**
     * duplikálja a megadott edzőtermet, annyi különbséggel, hogy az újnak az aktuális felhasználó lesz a létrehozója
     * @param gym edzőterem, mait duplikál
     * @return új duplikált edzőterem
     */
    @Transactional
    public Gym gymChanged(Gym gym){
        Gym newGym = Gym.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(gym.getName())
                .location(gym.getLocation())
                .publiclyAvailable(false)
                .howEquipped(gym.getHowEquipped())
                .build();
        newGym.setSplit(Split.builder()
                .name(Split.SplitType.valueOf(gym.getSplit().getName().toString()))
                .numberOfDays(gym.getSplit().getNumberOfDays())
                .gym(newGym)
                .build());

        registeredUserService.addNewGymToUser(newGym);
        for(Workout workouts: gym.getWorkouts()){
            addExistingWorkoutToGym(newGym.getId(), workouts.getId());
        }

        registeredUserService.removeGymFromUser(gym.getId());
        TrackerApplication.getInstance().setCurrentGym(newGym);
        return newGym;
    }

    /**
     * kilistázza egy edzőteremhez tartozó edzőterveket
     * @param id a keresett edzőterem id-ja
     * @return az edzőtervek listája
     */
    public List<Workout> listWorkouts(int id){
        Optional<Gym> gym = gymRepository.findById(id);
        return gym.get().getWorkouts();
    }

    /**
     * kitörli az összes edzőtermet az adatbázisból
     */
    @Transactional
    public void deleteAll(){
        gymRepository.deleteAllInBatch();
    }

    /**
     * megkeres egy edzőtermet az adatbázisban, majd Rateable-ként visszaadja
     * @param id a keresett edzőterem id-ja
     * @return a keresett edzőterem Rateableként
     */
    @Override
    public Rateable findById(int id) {
        return gymRepository.findById(id).get();
    }
}
