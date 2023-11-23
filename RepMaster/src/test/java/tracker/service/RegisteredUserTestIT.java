package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import tracker.TrackerApplication;
import tracker.model.*;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
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

        RegisteredUser rU = registeredUserService.findUserByName("TestUser3");
        assertThat(accountCreateMessage).isEqualTo("Account created, welcome TestUser3");
        assertThat(rU.getPassword()).isEqualTo("abc123");
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
        Workout workout = createWorkout();

        registeredUserService.rate(workout, 4.1, "nem rossz, lehetne jobb");

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().size()).isEqualTo(1);
        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().get(0).getComment()).isEqualTo("nem rossz, lehetne jobb");
        assertThat(workoutService.findWorkout(workout.getId()).getRatings().size()).isEqualTo(1);
        assertThat(workoutService.findWorkout(workout.getId()).getRatings().get(0).getComment()).isEqualTo("nem rossz, lehetne jobb");
    }

    @Test
    public void rateAnExercise() throws Exception{
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Exercise exercise = createExercise("Bench Press" , MuscleGroup.Middle_Chest);

        registeredUserService.rate(exercise, 5, "best");

        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().size()).isEqualTo(1);
        assertThat(TrackerApplication.getInstance().getLoggedInUser().getRatings().get(0).getComment()).isEqualTo("best");
        assertThat(exerciseService.findExercise(exercise.getId()).getRatings().size()).isEqualTo(1);
        assertThat(exerciseService.findExercise(exercise.getId()).getRatings().get(0).getComment()).isEqualTo("best");
    }

    @Test
    public void searchExerciseByMuscleGroup() throws Exception {
        createExercise("Bench Press" , MuscleGroup.Middle_Chest);
        Exercise cableFly = createExercise("Cable Fly" , MuscleGroup.Middle_Chest);
        List<Exercise> exercises = registeredUserService.SearchExerciseByMuscleGroup(MuscleGroup.Middle_Chest, null);


        assertThat(exercises.size()).isEqualTo(2);
        assertThat(exercises.get(0).getName()).isEqualTo("Bench Press");
        assertThat(exercises.get(1).getName()).isEqualTo("Cable Fly");
    }

    @Test
    public void searchExerciseByMuscleGroupAndDescendingRating() throws Exception {
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Exercise benchPress = createExercise("Bench Press" , MuscleGroup.Middle_Chest);
        Exercise cableFly = createExercise("Cable Fly" , MuscleGroup.Middle_Chest);

        registeredUserService.rate(benchPress, 4, "almost it");
        registeredUserService.rate(cableFly, 5, "best");
        String sortMode = "RatingDesc";
        List<Exercise> exercises = registeredUserService.SearchExerciseByMuscleGroup(MuscleGroup.Middle_Chest, sortMode);

        assertThat(exercises.get(0).getName()).isEqualTo("Cable Fly");
        assertThat(exercises.get(1).getName()).isEqualTo("Bench Press");
    }

    @Test
    public void searchExerciseByAnyMuscleGroup() throws Exception {
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Exercise benchPress = createExercise("Bench Press" , MuscleGroup.Middle_Chest);
        Exercise deadlift = createExercise("Deadlift" , MuscleGroup.Lower_Back);

        registeredUserService.rate(benchPress, 4, "almost it");
        registeredUserService.rate(deadlift, 5, "best");
        List<Exercise> exercises = registeredUserService.SearchExerciseByMuscleGroup(null, null);

        assertThat(exercises.size()).isEqualTo(2);
        assertThat(exercises.get(0).getName()).isEqualTo("Bench Press");
        assertThat(exercises.get(1).getName()).isEqualTo("Deadlift");
    }

    @Test
    public void testRatingCalculation() throws Exception{
        registeredUserService.loginUser(rUser.getUserName(), rUser.getPassword());
        Exercise benchPress = createExercise("Bench Press" , MuscleGroup.Middle_Chest);
        registeredUserService.rate(benchPress, 4, "almost it");
        registeredUserService.rate(benchPress, 5, "best");

        double avgRating = Rating.calculateRating(benchPress);
        assertThat(avgRating).isEqualTo(4.5);

    }

    public Exercise createExercise(String name, MuscleGroup muscleGroup){
        Exercise exercise = Exercise.builder().name(name)
                .primaryMuscleGroup(muscleGroup)
                .build();

        exerciseService.saveExercise(exercise);
        return exercise;
    }

    private Workout createWorkout() {
        Workout workout = Workout.builder().build();
        workoutService.saveWorkout(workout);
        return workout;
    }


}
