package tracker.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tracker.model.*;
import tracker.repository.GymRepository;
import tracker.repository.WorkoutRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GymServiceTest {
    @InjectMocks
    GymService gymService;
    @Mock
    GymRepository gymRepository;
    @Mock
    WorkoutRepository workoutRepository;
    private Gym gym;
    private Gym gym2;
    Workout workout1;
    Workout workout2;
    Workout workout3;

    @BeforeEach
    public void createWorkout(){
        //ARRANGE
        initGyms();
        initWorkouts();
    }

    @Test
    void testCreateGyms() {
        //Arrange
        when(gymRepository.save(any())).thenReturn(gym, gym2);
        when(gymRepository.findById(any())).thenReturn(Optional.of(gym), Optional.of(gym), Optional.of(gym2));

        //Act
        Gym copy_gym = gymService.saveGym(gym);
        Gym copy_gym2 = gymService.saveGym(gym2);

        //Assert
        assertThat(copy_gym.getName()).isEqualTo(gym.getName());
        assertThat(copy_gym2.getName()).isEqualTo(gym2.getName());
        assertThat(gymService.findGym(gym.getId()).getName()).isEqualTo(gym.getName());
        assertThat(gymService.findGym(gym2.getId()).getName()).isEqualTo(gym2.getName());
        verify(gymRepository, times(2)).save(any());
    }

    @Test
    void testAddNewWorkoutsToGyms() throws Exception{
        //Arrange
        when(workoutRepository.save(any())).thenReturn(workout1, workout2, workout3);
        when(gymRepository.findById(any())).thenReturn(Optional.of(gym));

        //ACT
        gymService.addNewWorkoutToGym(gym.getId(), workout1);
        gymService.addNewWorkoutToGym(gym.getId(), workout2);
        gymService.addNewWorkoutToGym(gym.getId(), workout3);

        //ASSERT
        List<Workout> gym1Workouts = gymService.listWorkouts(gym.getId());

        assertThat(gym1Workouts.size()).isEqualTo(3);
        assertThat(gym1Workouts.get(0).getId()).isEqualTo(workout1.getId());
        assertThat(gym1Workouts.get(1).getId()).isEqualTo(workout2.getId());
        assertThat(gym1Workouts.get(2).getId()).isEqualTo(workout3.getId());
        verify(workoutRepository, times(3)).save(any());
    }

    void initGyms(){
        gym = Gym.builder().name("TestGym1")
                        .location("Budapest")
                        .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                        .howEquipped(Equipment.Well_Equipped).build();
        gym2 = Gym.builder().name("TestGym2")
                        .location("Velence")
                        .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                        .howEquipped(Equipment.Fully_Equipped).build();
    }

    void initWorkouts(){
        workout1 = Workout.builder().name("Back").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Upper_Back, MuscleGroup.Lower_Back))).build();
        workout2 = Workout.builder().name("Biceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Biceps))).build();
        workout3 = Workout.builder().name("Triceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Triceps))).build();
    }
}
