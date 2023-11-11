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

        when(workoutRepository.findByName("Push")).thenReturn(
                Arrays.asList(pushWorkout)
        );

        when(workoutRepository.save(any())).thenAnswer(inv -> inv.getArguments()[0]);

        //Act
        workoutService.saveWorkout(pushWorkout);

        //Assert
        assertThat(pushWorkout.getExercises().get(0).getName()).isEqualTo("Bench Press");
        assertThat(pushWorkout.getExercises().get(1).getName()).isEqualTo("Shoulder Press");
        verify(workoutRepository, times(1)).save(any());
    }



}
