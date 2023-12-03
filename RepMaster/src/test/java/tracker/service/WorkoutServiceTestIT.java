package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.Exercise;
import tracker.model.Workout;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class WorkoutServiceTestIT {
    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseService exerciseService;

    private Workout pushWorkout;

    @BeforeEach
    public void createWorkout(){
        exerciseService.deleteAll();
        workoutService.deleteAll();

        pushWorkout = Workout.builder()
                .name("Push")
                .exercises(Arrays.asList(
                        Exercise.builder().name("Bench Press").build(),
                        Exercise.builder().name("Shoulder Press").build()
                )).build();
    }

    @Test
    void testSaveWorkout() {
        //Arrange

        //Act
        workoutService.saveWorkout(pushWorkout);

        //Assert
        assertThat(pushWorkout.getExercises().get(0).getName()).isEqualTo("Bench Press");
        assertThat(pushWorkout.getExercises().get(1).getName()).isEqualTo("Shoulder Press");
    }

    @Test
    void testListWorkouts(){
        //Arrange
        Workout pullWorkout = Workout.builder()
                .name("Pull")
                .exercises(Arrays.asList(
                        Exercise.builder().name("Biceps Curl").build(),
                        Exercise.builder().name("Lat Pulldown").build()
                )).build();

        //Act
        workoutService.saveWorkout(pushWorkout);
        workoutService.saveWorkout(pullWorkout);
        List<Workout> workouts = workoutService.listWorkouts();

        //Assert
        assertThat(workouts.get(0).getName()).isEqualTo("Push");
        assertThat(workouts.get(1).getName()).isEqualTo("Pull");
        assertThat(workouts.size()).isEqualTo(2);
    }

    @Test
    void testDeleteWorkout() {
        //Arrange
        workoutService.saveWorkout(pushWorkout);

        //Act
        workoutService.deleteW(pushWorkout.getId());
        List<Workout> workouts = workoutService.listWorkouts();

        //Assert
        assertThat(workouts.size()).isEqualTo(0);
    }
}
