package tracker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RegisteredUser extends User {
    private String password;
    private List<Gym> userGyms;
    private List<Workout> userWorkouts;

    public RegisteredUser(String username, String password) {
        super(username);
        this.password = password;
    }

    private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }

    public void addGym(Gym g) {
        if(userGyms == null) userGyms = new ArrayList<>();
        userGyms.add(g);
    }

    public void addWorkoutToUser(Workout w) {
        createWorkoutListIfEmpty();
        userWorkouts.add(w);
    }

    public void addWorkoutsFromUsersGyms() {
        createWorkoutListIfEmpty();
        for(Gym g: userGyms) {
            List<Workout> workouts = g.getWorkouts();
            userWorkouts.addAll(workouts);
        }
    }

    public void rateWorkout(Workout w, int rating) {
        w.addRating(rating);
    }

}
