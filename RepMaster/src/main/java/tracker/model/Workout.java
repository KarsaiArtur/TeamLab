package tracker.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workout")
    private List<Rating> ratings;

    @ManyToMany(mappedBy = "workouts", fetch = FetchType.EAGER)
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
            joinColumns = @JoinColumn(name = "ru_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<RegisteredUser> registeredUsers;

    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();
        e.addWorkout(this);
        exercises.add(e);
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

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setWorkout(null);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }

}
