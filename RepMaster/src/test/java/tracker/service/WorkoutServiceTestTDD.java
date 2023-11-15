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

    private Workout pushWorkout;
    private Exercise Bench_Press;

    @BeforeEach
    public void initDb(){
        exerciseService.deleteAll();
        workoutService.deleteAll();

        pushWorkout = Workout.builder().name("Push").build();
        Bench_Press = Exercise.builder().name("Bench Press")
                .isCompound(true)
                .primaryMuscleGroup(MuscleGroup.Middle_Chest)
                .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Upper_Chest, MuscleGroup.Lower_Chest, MuscleGroup.Anterior_Deltoids, MuscleGroup.Triceps)))
                .build();

        workoutService.saveWorkout(pushWorkout);
    }

    @Test
    void testAddNewExerciseToWorkout(){
        //Arrange


        //Act
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).get(0).getName()).isEqualTo("Bench Press");
    }

    @Test
    void testAddNewExerciseWithMuscleGroupToWorkout(){
        //Arrange

        //Act
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).get(0).getName()).isEqualTo("Bench Press");
        assertThat(workoutService.listMuscleGroups(pushWorkout.getId()).get(0)).isEqualTo(MuscleGroup.Middle_Chest);
    }

    @Test
    void testAddExistingExerciseWithMuscleGroupToWorkout(){
        //Arrange
        Exercise Lateral_raise = Exercise.builder().name("Lateral Raise")
                .isCompound(false)
                .primaryMuscleGroup(MuscleGroup.Lateral_Deltoids)
                .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Anterior_Deltoids)))
                .build();

        //Act
        exerciseService.saveExercise(Lateral_raise);
        workoutService.addExistingExerciseToWorkout(pushWorkout.getId(), Lateral_raise.getId());

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).get(0).getName()).isEqualTo("Lateral Raise");
        assertThat(workoutService.listMuscleGroups(pushWorkout.getId()).get(0)).isEqualTo(MuscleGroup.Lateral_Deltoids);
    }

    @Test
    void testRemoveExerciseFromWorkout(){
        //Arrange
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Act
        workoutService.removeExerciseFromWorkout(pushWorkout.getId(), Bench_Press.getId());

        //Assert
        assertThat(workoutService.listExercises(pushWorkout.getId()).size()).isEqualTo(0);
    }

    @Test
    void testRemoveExerciseWithMuscleGroupFromWorkout(){
        //Arrange
        workoutService.addNewExerciseToWorkout(pushWorkout.getId(), Bench_Press);

        //Act
        workoutService.removeExerciseFromWorkout(pushWorkout.getId(), Bench_Press.getId());

        //Assert
        assertThat(workoutService.listMuscleGroups(pushWorkout.getId()).size()).isEqualTo(0);
    }
}
