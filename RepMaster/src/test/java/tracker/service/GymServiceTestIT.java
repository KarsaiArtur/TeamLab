package tracker.service;


import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tracker.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GymServiceTestIT {
    @Autowired
    private GymService gymService;


    @BeforeEach
    public void clearDB() {
        gymService.deleteAll();
    }

    @Test
    void createGymAndWorkouts() throws Exception{
        //ARRANGE
        Gym gym = gymService.saveGym(
                Gym.builder().name("TestGym1")
                .location("Budapest")
                .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                .howEquipped(Equipment.Well_Equipped).build()
        );
        Gym gym2 = gymService.saveGym(
                Gym.builder().name("TestGym2")
                .location("Velence")
                .split(Split.builder().name(Split.SplitType.Body_Part).numberOfDays(5).build())
                .howEquipped(Equipment.Fully_Equipped).build()
        );

        Workout workout1 = Workout.builder().name("Back").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Upper_Back, MuscleGroup.Lower_Back))).build();
        Workout workout2 = Workout.builder().name("Biceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Biceps))).build();
        Workout workout3 = Workout.builder().name("Triceps").muscleGroups(new ArrayList<MuscleGroup>(Arrays.asList(MuscleGroup.Triceps))).build();

        gymService.addNewWorkoutToGym(gym.getName(), workout1);
        gymService.addNewWorkoutToGym(gym.getName(), workout2);
        gymService.addNewWorkoutToGym(gym2.getName(), workout3);
    }



    @Ignore
    void addWorkout() {

    }

    @Ignore
    void deleteWorkout () throws Exception{
        gymService.removeWorkoutFromGym("test2", "test2");



    }

    @Ignore
    void add(){
        gymService.addExistingWorkoutToGym("test2", "test2");
    }
}
