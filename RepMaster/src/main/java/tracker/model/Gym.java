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
public class Gym {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "split_id", referencedColumnName = "id")
    private Split split;
    @OneToMany(mappedBy = "gym")
    private List<Rating> ratings;
    @ManyToOne
    private Equipment howEquipped;
    private String location;
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

    public void addRating(double r, String comment) {
        ratings.add(new Rating(r, comment));
    }
}
