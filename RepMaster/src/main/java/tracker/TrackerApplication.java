package tracker;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.service.InitDbService;

@RequiredArgsConstructor
@SpringBootApplication
@Getter
@Setter
public class TrackerApplication implements CommandLineRunner{
    private static final TrackerApplication INSTANCE = new TrackerApplication();
    private RegisteredUser loggedInUser = RegisteredUser.builder().build();
    private boolean loggedIn = false;
    private Gym currentGym = Gym.builder().build();
    private Workout currentWorkout = Workout.builder().build();
    private Exercise currentExercise = Exercise.builder().build();
    private Rateable currentRateable = Gym.builder().build();

    public static TrackerApplication getInstance() { return INSTANCE; }

    @Override
    public void run(String... args) throws Exception{

    }

    public static void main(String[] args){
        SpringApplication.run(TrackerApplication.class, args);
    }

    public void reset(){
        setLoggedInUser(RegisteredUser.builder().build());
        setLoggedIn(false);
        setCurrentRateable(null);
        setCurrentGym(null);
        setCurrentWorkout(null);
        setCurrentExercise(null);
    }

}