package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.Gym;
import tracker.model.Workout;
import tracker.repository.WorkoutRepository;

@SpringBootTest
public class GymServiceTestIT {
    @Autowired
    private GymService gymService;


    @BeforeEach
    public void clearDB() {
        //gymService.deleteAll();
    }

    @Ignore
    void addWorkout() {
        Gym gym = Gym.builder().name("test2").build();
        gymService.saveGym(gym);
        Workout workout = Workout.builder().name("test2").build();
        gymService.addNewWorkoutToGym(gym.getName(), workout);
    }

    @Ignore
    void deleteWorkout () throws Exception{
        gymService.removeWorkoutFromGym("test2", "test2");



    }

    @Test
    void add(){
        gymService.addExistingWorkoutToGym("test2", "test2");
    }
}
