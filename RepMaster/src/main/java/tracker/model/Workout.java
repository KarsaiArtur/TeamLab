package tracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Workout implements Rateable{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(mappedBy = "workout")
    private List<Rating> ratings;
    @OneToMany(mappedBy = "workout")
    private List<Exercise> exercises;
    @ManyToMany(mappedBy = "workouts")
    private List<MuscleGroup> muscleGroups;
    @ManyToMany(mappedBy = "workouts")
    private List<Gym> gyms;
    /*????????????????????????
    @ManyToMany(mappedBy = "userWorkouts")
    private List<RegisteredUser> registeredUsers;*/


    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();

        exercises.add(e);
    }

    public void addMuscleGroup(MuscleGroup m){
        if(muscleGroups == null)
            muscleGroups = new ArrayList<>();

        muscleGroups.add(m);
    }

    @Override
    public void addRating(double r, String comment) {
        ratings.add(new Rating(r, comment));
    }

}
