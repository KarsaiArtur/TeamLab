package tracker.model;

import jakarta.persistence.*;
import lombok.*;
import tracker.repository.RegisteredUserRepository;

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "gym")
    private List<Rating> ratings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "split_id", referencedColumnName = "id")
    private Split split;

    @Enumerated(EnumType.STRING)
    private Equipment howEquipped;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "gym_workout_connection",
            joinColumns = @JoinColumn(name = "gym_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id")
    )
    private List<Workout> workouts;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name = "registered_user_connection",
            joinColumns = @JoinColumn(name = "registeredUser_id"),
            inverseJoinColumns = @JoinColumn(name = "gym_id")
    )
    private List<RegisteredUser> registeredUsers;

    public void addWorkout(Workout w){
        if(workouts == null)
            workouts = new ArrayList<>();

        workouts.add(w);
    }

    public void addRegisteredUser(RegisteredUser rU){
        if(registeredUsers == null)
            registeredUsers = new ArrayList<>();

        registeredUsers.add(rU);
    }

    @Override
    public void addRating(Rating r) {
        if(ratings == null)
            ratings = new ArrayList<>();
        ratings.add(r);
        r.setGym(this);
    }

    @Override
    public void removeRating(Rating r) {
        ratings.remove(r);
        r.setGym(null);
    }

    @Override
    public List<Rating> getRatings(){
        return ratings;
    }

    public void removeWorkout(Workout w){
        workouts.remove(w);
    }
}
