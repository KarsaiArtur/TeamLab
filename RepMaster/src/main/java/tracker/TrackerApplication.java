package tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tracker.model.*;

/**
 * A RepTracker program osztálya
 */
@RequiredArgsConstructor
@SpringBootApplication
@Getter
@Setter
public class TrackerApplication implements CommandLineRunner{
    /**
     * a program egyetlen példánya
     */
    private static final TrackerApplication INSTANCE = new TrackerApplication();
    /**
     * jelenleg bejelentkezett felhasználó
     */
    private RegisteredUser loggedInUser = RegisteredUser.builder().build();
    /**
     * be vannak-e jelentkezve
     */
    private boolean loggedIn = false;
    /**
     * jelenleg kiválasztott edzőterem
     */
    private Gym currentGym = Gym.builder().build();
    /**
     * jelenleg kiválasztott edzőterv
     */
    private Workout currentWorkout = Workout.builder().build();
    /**
     * jelenleg kiválasztott gyakorlat
     */
    private Exercise currentExercise = Exercise.builder().build();
    /**
     * jelenleg kiválasztott értékelhető objektum(edzőterem, edzőterv, gyakorlat)
     */
    private Rateable currentRateable = Gym.builder().build();

    /**
     * csak egy példány létezhet belőle
     * @return a program egyetlen példánya
     */
    public static TrackerApplication getInstance() { return INSTANCE; }

    /**
     * a kód amit a program futtat
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception{
    }

    /**
     * a main lefuttatja a program kódját
     * @param args
     */
    public static void main(String[] args){
        SpringApplication.run(TrackerApplication.class, args);
    }

    /**
     * kijelentkezésnél az attribútumok visszaállítása alap értékre
     */
    public void reset(){
        setLoggedInUser(RegisteredUser.builder().build());
        setLoggedIn(false);
        setCurrentRateable(null);
        setCurrentGym(null);
        setCurrentWorkout(null);
        setCurrentExercise(null);
    }
}