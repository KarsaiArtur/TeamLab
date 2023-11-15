package tracker.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import tracker.TrackerApplication;
import tracker.model.Exercise;
import tracker.model.MuscleGroup;
import tracker.model.RegisteredUser;
import tracker.model.Workout;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
public class RegisteredUserTestIT {
    @Autowired
    private RegisteredUserService registeredUserService;
    @Autowired
    private WorkoutService workoutService;
    @Autowired
    private ExerciseService exerciseService;
    private RegisteredUser rUser;

    @BeforeEach
    public void clearAndInitDb() {
        registeredUserService.deleteAll();
        rUser = RegisteredUser.builder().userName("TestUser").password("abc123").build();
        registeredUserService.addRegisteredUser(rUser);
    }

    @Test
    public void createUserWithExistingName() throws Exception{
        String accountCreateMessage = registeredUserService.addRegisteredUser(RegisteredUser.builder().userName("TestUser").password("abc123").build());

        assertThat(accountCreateMessage).isEqualTo("Account creation failed: username already exists");
    }

    @Test
    public void createNewUser() throws Exception{
        String accountCreateMessage = registeredUserService.addRegisteredUser(RegisteredUser.builder().userName("TestUser3").password("abc123").build());

        assertThat(accountCreateMessage).isEqualTo("Account created, welcome TestUser3");
    }

    @Test
    public void loginAnExistingUser() throws Exception{
        String loginMessage = registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());

        assertThat(loginMessage).isEqualTo("Login successful as "+rUser.getUserName());
    }

    @Test
    public void loginANonExistingUser() throws Exception{
        String loginMessage = registeredUserService.loginUser("TestUser2", "");

        assertThat(loginMessage).isEqualTo("Login failed: no User with such username");
    }

    @Test
    public void loginWithWrongPassword() throws Exception{
        String loginMessage = registeredUserService.loginUser(rUser.getUserName(), "");

        assertThat(loginMessage).isEqualTo("Login failed: wrong password");
    }

    @Test
    public void checkIfUserIsLoggedIn() throws Exception{
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getUserName()).isEqualTo(rUser.getUserName());
    }

    @Test
    public void signOutUser() throws Exception{
        registeredUserService.singOutUser();

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getUserName()).isNull();
    }

    @Test
    public void rateAWorkout() throws Exception{
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Workout workout = Workout.builder()
                .name("Push")
                .exercises(Arrays.asList(
                        Exercise.builder().name("Bench Press").build(),
                        Exercise.builder().name("Shoulder Press").build()
                )).build();
        workoutService.saveWorkout(workout);

        registeredUserService.rate(workout, 4.1, "nem rossz, lehetne jobb");

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().size()).isEqualTo(1);
        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().get(0).getComment()).isEqualTo("nem rossz, lehetne jobb");
        assertThat(workoutService.findWorkout(workout.getId()).getRatings().size()).isEqualTo(1);
        assertThat(workoutService.findWorkout(workout.getId()).getRatings().get(0).getComment()).isEqualTo("nem rossz, lehetne jobb");
    }

    @Test
    public void rateAnExercise() throws Exception{
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Exercise exercise = Exercise.builder().name("Bench Press")
                .isCompound(true)
                .primaryMuscleGroup(MuscleGroup.Middle_Chest)
                .secondaryMuscleGroups(new ArrayList<>(Arrays.asList(MuscleGroup.Upper_Chest, MuscleGroup.Lower_Chest)))
                .build();
        exerciseService.saveExercise(exercise);

        registeredUserService.rate(exercise, 5, "best");

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().size()).isEqualTo(1);
        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().get(0).getComment()).isEqualTo("best");
        assertThat(exerciseService.findExercise(exercise.getId()).getRatings().size()).isEqualTo(1);
        assertThat(exerciseService.findExercise(exercise.getId()).getRatings().get(0).getComment()).isEqualTo("best");
    }





}
