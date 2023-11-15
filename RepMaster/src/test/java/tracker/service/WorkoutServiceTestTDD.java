package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;
import tracker.model.Workout;
import tracker.repository.ExerciseRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class WorkoutServiceTestTDD {
    @Autowired
    private WorkoutService workoutService;

    @Autowired
    private ExerciseService exerciseService;

    @BeforeEach
    public void clearDb(){
        exerciseService.deleteAll();
        workoutService.deleteAll();
    }

    @Test
    void testAddNewExerciseToWorkout(){
        //Arrange
        Workout pushWorkout = Workout.builder().name("Push").build();
        Exercise Bench_Press = Exercise.builder().name("Bench Press")
                .isCompound(true)
                .primaryMuscleGroup(MuscleGroup.Middle_Chest)
                .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Upper_Chest, MuscleGroup.Lower_Chest, MuscleGroup.Anterior_Deltoids, MuscleGroup.Triceps)))
                .build();

        //Act
        workoutService.saveWorkout(pushWorkout);
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).get(0).getName()).isEqualTo("Bench Press");
    }

    @Test
    void testAddNewMuscleGroupToWorkout(){
        //Arrange
        Workout pushWorkout = Workout.builder().name("Push").build();

        //Act
        workoutService.saveWorkout(pushWorkout);
        workoutService.addMuscleGroupToWorkout(pushWorkout.getId(), MuscleGroup.Middle_Chest);

        //Assert
        assertThat(workoutService.listMuscleGroups(pushWorkout.getId()).get(0)).isEqualTo(MuscleGroup.Middle_Chest);
    }

    @Test
    void testAddNewExerciseWithMuscleGroupToWorkout(){
        //Arrange
        Workout pushWorkout = Workout.builder().name("Push").build();
        Exercise Bench_Press = Exercise.builder().name("Bench Press")
                .isCompound(true)
                .primaryMuscleGroup(MuscleGroup.Middle_Chest)
                .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Upper_Chest, MuscleGroup.Lower_Chest, MuscleGroup.Anterior_Deltoids, MuscleGroup.Triceps)))
                .build();

        //Act
        workoutService.saveWorkout(pushWorkout);
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).get(0).getName()).isEqualTo("Bench Press");
        assertThat(workoutService.listMuscleGroups(pushWorkout.getId()).get(0)).isEqualTo(MuscleGroup.Middle_Chest);
    }
}
