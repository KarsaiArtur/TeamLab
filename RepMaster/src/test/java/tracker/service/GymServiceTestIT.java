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
public class GymServiceTestIT {
    @Autowired
    private GymService gymService;
    @Autowired
    private WorkoutService workoutService;

    private Gym gym;
    private Gym gym2;
    Workout workout1;
    Workout workout2;
    Workout workout3;

    @BeforeEach
    public void clearAndInitDb() {
        workoutService.deleteAll();
        gymService.deleteAll();

        //ARRANGE
        initGyms();
        initWorkouts();
    }

    @Test()
    void testCreateGyms() throws Exception{
        //ASSERT
        List<Gym> gyms = gymService.listGyms();
        assertThat(gyms.size()).isEqualTo(2);
        assertThat(gyms.get(0).getId()).isEqualTo(gym.getId());
        assertThat(gyms.get(1).getId()).isEqualTo(gym2.getId());
    }

    @Test()
    void testDeleteGym() throws Exception{
        gymService.deleteGym(gym.getId());

        //ASSERT
        List<Gym> gyms = gymService.listGyms();
        assertThat(gyms.size()).isEqualTo(1);
        assertThat(gyms.get(0).getId()).isEqualTo(gym2.getId());
        assertThat(gymService.findGym(gym.getId())).isNull();
    }

    @Test
    void testAddNewWorkoutsToGyms() throws Exception{
        //ACT
        gymService.addNewWorkoutToGym(gym.getId(), workout1);
        gymService.addNewWorkoutToGym(gym.getId(), workout2);
        gymService.addNewWorkoutToGym(gym2.getId(), workout3);

        //ASSERT
        List<Workout> gym1Workouts = gymService.listWorkouts(gym.getId());
        List<Workout> gym2Workouts = gymService.listWorkouts(gym2.getId());

        assertThat(gym1Workouts.size()).isEqualTo(2);
        assertThat(gym2Workouts.size()).isEqualTo(1);
        assertThat(gym1Workouts.get(0).getId()).isEqualTo(workout1.getId());
        assertThat(gym1Workouts.get(1).getId()).isEqualTo(workout2.getId());
        assertThat(gym2Workouts.get(0).getId()).isEqualTo(workout3.getId());
    }

    @Test
    void testAddExistingWorkoutsToGyms() {
        //ACT
        gymService.addNewWorkoutToGym(gym.getId(), workout1);
        gymService.addNewWorkoutToGym(gym.getId(), workout2);
        gymService.addNewWorkoutToGym(gym2.getId(), workout3);
        gymService.addExistingWorkoutToGym(gym2.getId(), workout1.getId());
        gymService.addExistingWorkoutToGym(gym2.getId(), workout2.getId());

        //ASSERT
        List<Workout> gym1Workouts = gymService.listWorkouts(gym.getId());
        List<Workout> gym2Workouts = gymService.listWorkouts(gym2.getId());

        assertThat(gym1Workouts.size()).isEqualTo(2);
        assertThat(gym2Workouts.size()).isEqualTo(3);
        assertThat(gym1Workouts.get(0).getId()).isEqualTo(workout1.getId());
        assertThat(gym1Workouts.get(1).getId()).isEqualTo(workout2.getId());
        assertThat(gym2Workouts.get(0).getId()).isEqualTo(workout3.getId());
        assertThat(gym2Workouts.get(1).getId()).isEqualTo(workout1.getId());
        assertThat(gym2Workouts.get(2).getId()).isEqualTo(workout2.getId());
    }

    @Test()
    void testRemoveWorkoutFromGym () throws Exception{
        //ACT
        gymService.addNewWorkoutToGym(gym.getId(), workout1);
        gymService.addNewWorkoutToGym(gym.getId(), workout2);
        gymService.addNewWorkoutToGym(gym2.getId(), workout3);
        gymService.removeWorkoutFromGym(gym.getId(), workout2.getId());

        //ASSERT
        List<Workout> gym1Workouts = gymService.listWorkouts(gym.getId());
        List<Workout> gym2Workouts = gymService.listWorkouts(gym2.getId());

        assertThat(gym1Workouts.size()).isEqualTo(1);
        assertThat(gym2Workouts.size()).isEqualTo(1);
        assertThat(gym1Workouts.get(0).getId()).isEqualTo(workout1.getId());
        assertThat(gym2Workouts.get(0).getId()).isEqualTo(workout3.getId());

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            gym1Workouts.get(1).getId();
        });
    }

    void initGyms(){
        gym = gymService.saveGym(
                Gym.builder().name("TestGym1")
                        .location("Budapest")
                        .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                        .howEquipped(Equipment.Well_Equipped).build()
        );
        gym2 = gymService.saveGym(
                Gym.builder().name("TestGym2")
                        .location("Velence")
                        .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                        .howEquipped(Equipment.Fully_Equipped).build()
        );
    }

    void initWorkouts(){
        workout1 = Workout.builder().name("Back").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Upper_Back, MuscleGroup.Lower_Back))).build();
        workout2 = Workout.builder().name("Biceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Biceps))).build();
        workout3 = Workout.builder().name("Triceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Triceps))).build();
    }
}

