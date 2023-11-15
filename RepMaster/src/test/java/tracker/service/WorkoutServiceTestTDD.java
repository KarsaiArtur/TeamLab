package tracker.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.Exercise;
import tracker.model.Workout;
import tracker.repository.ExerciseRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class WorkoutServiceTestTDD {
    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void testAddNewExerciseToWorkout(){
        //Arrange
        Workout pushWorkout = Workout.builder().name("Push").build();
        Exercise Bench_Press = Exercise.builder().name("Bench Press").build();

        //Act
        workoutService.saveWorkout(pushWorkout);
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Assert
        assertThat(pushWorkout.getExercises().get(0).getName() == "Bench_Press");
        assertThat(exerciseRepository.findAll().get(0).getName() == "Bench_Press");
    }
}
