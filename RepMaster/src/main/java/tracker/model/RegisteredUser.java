package tracker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class RegisteredUser extends User {
    private String password;
    @OneToMany(mappedBy = "registeredUser")
    private List<Gym> userGyms;

    /*????????????????????????????
    @ManyToMany(mappedBy = "registeredUsers")
    private List<Workout> userWorkouts;*/

    public RegisteredUser(String username, String password) {
        super(username);
        this.password = password;
    }

    public RegisteredUser() {
        super();
    }

    /*private void createWorkoutListIfEmpty() {
        if(userWorkouts == null) userWorkouts = new ArrayList<>();
    }*/

    public void addGym(Gym g) {
        if(userGyms == null) userGyms = new ArrayList<>();
        userGyms.add(g);
    }

    /*public void addWorkoutToUser(Workout w) {
        createWorkoutListIfEmpty();
        userWorkouts.add(w);
    }*/

    /*public void addWorkoutsFromUsersGyms() {
        createWorkoutListIfEmpty();
        for(Gym g: userGyms) {
            List<Workout> workouts = g.getWorkouts();
            userWorkouts.addAll(workouts);
        }
    }*/

    /*public void rateWorkout(Workout w, double rating, String comment) {
        w.addRating(rating, comment);
    }
    public void rateGym(Gym g, double rating, String comment) {
        g.addRating(rating, comment);
    }

    public void rateExercise(Exercise e, double rating, String comment) {
        e.addRating(rating, comment);
    }*/

    public void rate(Object o, double rating, String comment) throws Exception{
        if(o instanceof Exercise) ((Exercise) o).addRating(rating, comment);
        else if(o instanceof Workout) ((Workout) o).addRating(rating,comment);
        else if(o instanceof Gym) ((Gym) o).addRating(rating, comment);
        else throw new Exception("What kind of object is this?");
    }

}
