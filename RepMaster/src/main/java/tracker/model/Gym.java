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
public class Gym implements Rateable{
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String location;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "split_id", referencedColumnName = "id")
    private Split split;

    @OneToMany(mappedBy = "gym")
    private List<Rating> ratings;

    @ManyToOne
    private Equipment howEquipped;

    @ManyToMany
    @JoinTable(
            name = "gym_workout_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<Workout> workouts;

    @ManyToOne
    private RegisteredUser registeredUser;

    public void addWorkout(Workout w){
        if(workouts == null)
            workouts = new ArrayList<>();

        workouts.add(w);
    }

    public void removeWorkout(Workout w){
        workouts.remove(w);
    }

    public void editGym(Gym editedGym){
        name = editedGym.getName();
        location = editedGym.getLocation();
        split = editedGym.getSplit();
        howEquipped = editedGym.getHowEquipped();
    }

    @Override
    public void addRating(double r, String comment) {
        ratings.add(new Rating(r, comment));
    }
}
