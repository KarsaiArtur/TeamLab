package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * a regisztrált felhasználó service osztálya, amelyben meg vannak valósítva a komplexebb függvények, amelyeket a webes réteg használ
 */
@RequiredArgsConstructor
@Service
public class RegisteredUserService {
    /**
     * a regisztrált felhasználókhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált felhasználókkal
     */
    private final RegisteredUserRepository registeredUserRepository;
    /**
     * az edzőtervekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált edzőtervekkel
     */
    private final WorkoutRepository workoutRepository;
    /**
     * az értékelésekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált értékelésekkel
     */
    private final RatingRepository ratingRepository;
    /**
     * a gyakorlatokhoz tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált gyakorlatokkal
     */
    private final ExerciseRepository exerciseRepository;
    /**
     * az edzőtermekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő regisztrált edzőtermekkel
     */
    private final GymRepository gymRepository;

    /**
     * segédfüggvény, amely ellenőrzi, hogy létezik-e egy adott felhasználónév az adatbázisban
     * @param userName a keresett felhasználónév
     * @return a talált érték
     */
    private boolean userNameDoesntExist(String userName){
        return findUserByName(userName) == null;
    }

    /**
     * segédfüggvény, amely ellenőrzi, hogy helyes-e a felhasználónévvel megadott jelszó
     * @param userName a megadott felhasználónév
     * @param password a megadott jelszó
     * @return a talált érték
     */
    private boolean wrongPassword(String userName, String password) {
        return !findUserByName(userName).getPassword().equals(password);
    }


    /**
     * megkeres egy felhasználót név alapján az adatbázisban
     * @param userName a keresett felhasználó neve
     * @return a keresett felhasználó (null ha nem létezik)
     */
    public RegisteredUser findUserByName(String userName){
        return registeredUserRepository.findByUserName(userName).size()==0 ? null : registeredUserRepository.findByUserName(userName).get(0);
    }

    /**
     * hozzáad a felhasználóhoz egy az adatbázisban még nem létező edzőtermet
     * @param gym a hozzáadott edzőterem
     */
    @Transactional
    public void addNewGymToUser(Gym gym){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        gymRepository.save(gym);
        registeredUser.get().addGym(gym);
    }

    /**
     * hozzáad a felhasználóhoz egy az adatbázisban már létező edzőtermet
     * @param gym_id a hozzáadott edzőterem id-ja
     */
    @Transactional
    public void addExistingGymToUser(int gym_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Gym> gym = gymRepository.findById(gym_id);
        registeredUser.get().addGym(gym.get());
    }

    /**
     * eltávolít a felhasználótól egy edzőtermet
     * @param gym_id az eltávolított edzőterem
     */
    @Transactional
    public void removeGymFromUser(int gym_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Gym> gym = gymRepository.findById(gym_id);
        if(!registeredUser.isEmpty())
            registeredUser.get().removeGym(gym.get());
    }

    /**
     * eltávolít a felhasználótól egy edzőtervet
     * @param workout_id az eltávolított edzőterv
     */
    @Transactional
    public void removeWorkoutFromUser(int workout_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Workout> workout = workoutRepository.findById(workout_id);
        registeredUser.get().removeWorkout(workout.get());
    }

    /**
     * hozzáad a felhasználóhoz egy az adatbázisban már létező edzőtervet
     * @param workout_id a hozzáadott edzőterv id-ja
     */
    @Transactional
    public void addExistingWorkoutToUser(int workout_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Workout> workout = workoutRepository.findById(workout_id);
        registeredUser.get().addWorkout(workout.get());
    }

    /**
     * egy felhasználó regisztrálása az adatbázisba. ha már létező felhasználónevet akar használni a felhasználó, sikertelen lesz
     * @param rUser a regisztrált felhasználó
     * @return sikeres/sikertelen regisztrálás üzenet
     */
    @Transactional
    public String addRegisteredUser(RegisteredUser rUser) {
        if(!userNameDoesntExist(rUser.getUserName()))
            return "Account creation failed: username already exists";
        registeredUserRepository.save(rUser);
        return "Account created, welcome "+rUser.getUserName();
    }

    /**
     * egy felhasználó bejelentkeztetése. ha rossz a megadott felhasználónév vagy jelszó, sikertelen lesz
     * @param userName a megadott felhasználónév
     * @param password a megadott jelszó
     * @return sikeres/sikertelen bejelentkezés üzenet
     */
    public String loginUser(String userName, String password) {
        if(userNameDoesntExist(userName))
            return "Login failed: no User with such username";
        if(wrongPassword(userName, password))
            return "Login failed: wrong password";
        TrackerApplication.getInstance().setLoggedInUser(findUserByName(userName));
        TrackerApplication.getInstance().setLoggedIn(true);
        return "Login successful as "+userName;
    }

    /**
     * egy felhasználó kijelentkeztetése
     */
    public void singOutUser(){
        TrackerApplication.getInstance().reset();
    }

    /**
     * a bejelentkezett felhasználó értékel egy Rateable objektumot
     * @param rateable az értékelhető objektum: gym, workout vagy exercise
     * @param rating 1-5 skálán egy értékelés
     * @param comment az értékeléshez fűzott komment
     */
    @Transactional
    public void rate(Rateable rateable, double rating, String comment){
        RegisteredUser rUser = TrackerApplication.getInstance().getLoggedInUser();
        Rating new_rating = Rating.builder().rating(rating).comment(comment).build();

        ratingRepository.save(new_rating);
        rUser.addRating(rateable, new_rating);
    }

    /**
     * a bejelentkezett felhasználó törli egy értékelését
     * @param rateable az értékelhető objektum: gym, workout vagy exercise
     * @param ratingId az értékelésének az id-ja
     */
    @Transactional
    public void deleteRating(Rateable rateable, int ratingId){
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        rateable.removeRating(rating.get());
        ratingRepository.deleteById(ratingId);
    }

    /**
     * ezen a függvényen keresztül kereshet a felhasználó izomcsoportra a publikus gyakorlatok között
     * @param muscleGroup a keresett izomcsoport, ha üres, akkor kilistázza az összes gyakorlatot
     * @param sortMode a rendezési mód: RatingDesc, RatingAsc, NameDesc, NameAsc
     * @return a talált gyakorlatok listája Rateable objektumokként
     */
    public List<Rateable> SearchExerciseByMuscleGroup(String muscleGroup, String sortMode){
        List<Exercise> exercises = exerciseRepository.findAll();
        if(muscleGroup != null){
            exercises = exercises.stream().filter(a -> a.getPrimaryMuscleGroup().toString().toUpperCase().contains(muscleGroup.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Exercise e: exercises) {
            rateables.add(e);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }

    /**
     * ezen a függvényen keresztül kereshet a felhasználó izomcsoport felbontásra a publikus edzőtermek között
     * @param split a keresett izomcsoport felbontás, ha üres, akkor kilistázza az összes edzőtermet
     * @param sortMode a rendezési mód: RatingDesc, RatingAsc, NameDesc, NameAsc
     * @return a talált edzőtermek listája Rateable objektumokként
     */
    public List<Rateable> SearchGymBySplit(String split, String sortMode){
        List<Gym> gyms = gymRepository.findAll();
        if(split != null){
            gyms = gyms.stream().filter(a -> a.getSplit().getName().toString().toUpperCase().contains(split.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Gym g: gyms) {
            rateables.add(g);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }

    /**
     * ezen a függvényen keresztül kereshet a felhasználó névre a publikus edzőtervek között
     * @param name a keresett név, ha üres, akkor kilistázza az összes edzőtervet
     * @param sortMode a rendezési mód: RatingDesc, RatingAsc, NameDesc, NameAsc
     * @return a talált edzőtervek listája Rateable objektumokként
     */
    public List<Rateable> SearchWorkoutByName(String name, String sortMode){
        List<Workout> workouts = workoutRepository.findAll();
        if(name != "" && name != null){
            workouts = workouts.stream().filter(a -> a.getName().toUpperCase().contains(name.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Workout w: workouts) {
            rateables.add(w);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }


    /**
     * a paraméterben kapott listából kiválasztja a publikusokat és a kapott rendezési mód alapján rendezi
     * @param rateables a keresésre kapott találatok listája
     * @param sortMode a rendezési mód: RatingDesc, RatingAsc, NameDesc, NameAsc
     * @return a szűrt, illetve rendezett lista
     */
    public List<Rateable> sortMode(List<Rateable> rateables, String sortMode){
        rateables = rateables.stream().filter(a -> a.isPubliclyAvailable()).collect(Collectors.toList());
        if("RatingDesc".equals(sortMode)){
            Collections.sort(rateables,
                    (o1, o2) -> Rating.calculateRating(o1) > Rating.calculateRating(o2) ? -1 : (Rating.calculateRating(o1) < Rating.calculateRating(o2)) ? 1 : 0);
        }
        if("RatingAsc".equals(sortMode)){
            Collections.sort(rateables,
                    (o1, o2) -> Rating.calculateRating(o1) < Rating.calculateRating(o2) ? -1 : (Rating.calculateRating(o1) > Rating.calculateRating(o2)) ? 1 : 0);
        }
        if("NameAsc".equals(sortMode)){
            Collections.sort(rateables,
                    (o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        if("NameDesc".equals(sortMode)){
            Collections.sort(rateables,
                    (o1, o2) -> o2.getName().compareTo(o1.getName()));
        }
        return rateables;
    }

    /**
     * Teszteléshez - kitörli az adatbázis tartalmát
     */
    public void deleteAll(){
        ratingRepository.deleteAllInBatch();
        registeredUserRepository.deleteAllInBatch();
        workoutRepository.deleteAll();
        exerciseRepository.deleteAllInBatch();
        gymRepository.deleteAllInBatch();
    }
}
