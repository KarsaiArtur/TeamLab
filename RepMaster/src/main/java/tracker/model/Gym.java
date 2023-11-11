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

    @Enumerated(EnumType.STRING)
    private Equipment howEquipped;

    @ManyToMany(fetch = FetchType.EAGER)
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

    @Override
    public void addRating(Rating r) {
        ratings.add(r);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
    }
}
