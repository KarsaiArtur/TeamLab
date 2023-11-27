package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import tracker.web.RateableDetailTLController;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Workout extends Rateable{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workout")
    private List<Rating> ratings;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "workout_exercise_connection",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    private List<Exercise> exercises;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="muscle_groups")
    @Column(name="muscle_group")
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> muscleGroups;

    @ManyToMany(mappedBy = "workouts", fetch = FetchType.EAGER)
    private List<Gym> gyms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "registered_user_workout_connection",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "registeredUser_id")
    )
    private List<RegisteredUser> registeredUsers;

    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();
        exercises.add(e);
        e.addWorkout(this);
    }

    public void removeExercise(Exercise e){
        exercises.remove(e);
    }

    public void addMuscleGroup(MuscleGroup m){
        if(muscleGroups == null)
            muscleGroups = new ArrayList<>();

        muscleGroups.add(m);
    }

    public void removeMuscleGroup(MuscleGroup m){
        muscleGroups.remove(m);
    }

    @Override
    public void addRating(Rating r) {
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setWorkout(this);
    }

    public void addRegisteredUser(RegisteredUser rU){
        if(registeredUsers == null)
            registeredUsers = new ArrayList<>();

        registeredUsers.add(rU);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setWorkout(null);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public List<RateableDetailTLController.Details> details(){
        double rating = Rating.calculateRating(this);
        List<RateableDetailTLController.Details> details = new ArrayList<>();
        return details;
    }
}
