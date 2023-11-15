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
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "workout")
    private List<Exercise> exercises;

    @ElementCollection
    @CollectionTable(name="muscle_groups")
    @Column(name="muscle_group")
    @Enumerated(EnumType.STRING)
    private List<MuscleGroup> muscleGroups;

    @ManyToMany(mappedBy = "workouts", fetch = FetchType.EAGER)
    private List<Gym> gyms;

    public void addExercise(Exercise e){
        if(exercises == null)
            exercises = new ArrayList<>();

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
        ratings.add(r);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
    }

}
