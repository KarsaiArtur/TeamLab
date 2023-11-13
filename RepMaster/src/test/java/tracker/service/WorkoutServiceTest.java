package tracker.service;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tracker.model.Exercise;
import tracker.model.Workout;
import tracker.repository.WorkoutRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {
    @InjectMocks
    WorkoutService workoutService;
    @Mock
    WorkoutRepository workoutRepository;

    @Test
    void testSaveWorkout() {
        //Arrange
        Workout pushWorkout = Workout.builder()
                .name("Push")
                .exercises(Arrays.asList(
                    Exercise.builder().name("Bench Press").build(),
                    Exercise.builder().name("Shoulder Press").build()
                )).build();

        when(workoutRepository.findById(pushWorkout.getId())).thenReturn(Optional.of(pushWorkout));

        when(workoutRepository.save(any())).thenAnswer(inv -> inv.getArguments()[0]);

        //Act
        workoutService.saveWorkout(pushWorkout);

        //Assert
        assertThat(pushWorkout.getExercises().get(0).getName()).isEqualTo("Bench Press");
        assertThat(pushWorkout.getExercises().get(1).getName()).isEqualTo("Shoulder Press");
        verify(workoutRepository, times(1)).save(any());
    }

    @Test
    void testListExercises(){
        //Arrange
        Workout pushWorkout = Workout.builder()
                .name("Push")
                .exercises(Arrays.asList(
                        Exercise.builder().name("Bench Press").build(),
                        Exercise.builder().name("Shoulder Press").build()
                )).build();

        when(workoutRepository.findById(pushWorkout.getId())).thenReturn(Optional.of(pushWorkout));

        //Act
        List<Exercise> pushExercises = workoutService.listExercises(pushWorkout.getId());

        //Assert
        assertThat(pushExercises.get(0).getName()).isEqualTo("Bench Press");
        assertThat(pushExercises.get(1).getName()).isEqualTo("Shoulder Press");
        assertThat(pushExercises.size()).isEqualTo(2);
    }



}
