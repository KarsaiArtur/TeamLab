package tracker.service;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase
public class ExerciseServiceIT {
    @Autowired
    private ExerciseService exerciseService;

    //@Autowired
    //private ExerciseResultService exerciseResultService;

    private Exercise exercise1;
    private Exercise exercise2;
    ExerciseResult result1;
    ExerciseResult result2;
    ExerciseResult result3;


    @BeforeEach
    private void clearAndInitDb() {
        //exerciseResultService.deleteAll();
        exerciseService.deleteAll();
        initExercises();
        initResults();
    }

    private void addNewResults() {
        exerciseService.addNewResult(exercise1.getId(), result1);
        exerciseService.addNewResult(exercise1.getId(), result2);
        exerciseService.addNewResult(exercise2.getId(), result3);
    }

    @Test
    public void addNewResultsToExercises() throws Exception {
        addNewResults();

        List<ExerciseResult> exercise1Results = exerciseService.listResults(exercise1.getId());
        List<ExerciseResult> exercise2Results = exerciseService.listResults(exercise2.getId());

        assertThat(exercise1Results.size()).isEqualTo(2);
        assertThat(exercise2Results.size()).isEqualTo(1);
        assertThat(exercise1Results.get(0).getId()).isEqualTo(result1.getId());
        assertThat(exercise1Results.get(1).getId()).isEqualTo(result2.getId());
        assertThat(exercise2Results.get(0).getId()).isEqualTo(result3.getId());
    }

    @Test
    public void addExistingResultsToExercises() throws Exception {
        addNewResults();

        exerciseService.addExistingResultToExercise(exercise2.getId(), result1.getId());
        exerciseService.addExistingResultToExercise(exercise2.getId(), result2.getId());

        List<ExerciseResult> exercise2Results = exerciseService.listResults(exercise2.getId());

        assertThat(exercise2Results.size()).isEqualTo(3);

        assertThat(exercise2Results.get(0).getId()).isEqualTo(result1.getId());
        assertThat(exercise2Results.get(1).getId()).isEqualTo(result2.getId());
        assertThat(exercise2Results.get(2).getId()).isEqualTo(result3.getId());

    }

    /*@Ignore
    public void deleteResult() throws Exception {
        addNewResults();
        exerciseService.removeResultFromExercise(exercise1.getId(), result2.getId());

        List<ExerciseResult> exercise1Results = exerciseService.listResults(exercise1.getId());

        assertThat(exercise1Results.size()).isEqualTo(1);
        assertThat(exercise1Results.get(0).getId()).isEqualTo(result1.getId());

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            exercise1Results.get(1).getId();
        });
    }*/

    /*@Ignore
    void testDeleteExercises() {
        exerciseService.saveExercise(exercise1);

        exerciseService.deleteExercise(exercise1.getId());
        List<Exercise> exercises = exerciseService.listExercises();

        assertThat(exercises.size()).isEqualTo(0);
    }*/

    private void initExercises() {
        exercise1 = exerciseService.saveExercise(
                Exercise.builder().name("Bench Press")
                        .isCompound(true)
                        .primaryMuscleGroup(MuscleGroup.Middle_Chest)
                        .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Upper_Chest, MuscleGroup.Lower_Chest)))
                        .build()
        );

        exercise2 = exerciseService.saveExercise(
                Exercise.builder().name("Hammer Curls")
                        .isCompound(false)
                        .primaryMuscleGroup(MuscleGroup.Biceps)
                        .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Anterior_Deltoids)))
                        .build()
        );
    }

    private void initResults(){
        result1 = ExerciseResult.builder().totalVolume(2800).build();
        result2 = ExerciseResult.builder().totalVolume(2000).build();
        result3 = ExerciseResult.builder().totalVolume(1200).build();
    }
}

