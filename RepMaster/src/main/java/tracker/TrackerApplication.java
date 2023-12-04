package tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tracker.model.*;

@RequiredArgsConstructor
@SpringBootApplication
@Getter
@Setter
/**
 * Program osztaly
 */
public class TrackerApplication implements CommandLineRunner{
    /**
     * a program egyetlen példánya
     */
    private static final TrackerApplication INSTANCE = new TrackerApplication();
    private RegisteredUser loggedInUser = RegisteredUser.builder().build();
    private boolean loggedIn = false;
    private Gym currentGym = Gym.builder().build();
    private Workout currentWorkout = Workout.builder().build();
    private Exercise currentExercise = Exercise.builder().build();
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