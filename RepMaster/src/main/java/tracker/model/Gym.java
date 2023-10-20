package tracker.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Gym {
    private int id;
    private String name;
    private Split split;
    private List<Workout> workouts;

    public void addWorkout(Workout w){
        if(workouts == null)
            workouts = new ArrayList<>();

        workouts.add(w);
    }
}
