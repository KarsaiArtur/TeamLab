package tracker.model;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Workout {
    private int id;
    private String name;
    private List<Integer> allRatings;
    private double rating;
    private List<Exercise> exercises;
    private List<MuscleGroup> muscleGroups;

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

    public void addRating(int rating) {
        if(allRatings == null) allRatings = new ArrayList<>();
        allRatings.add(rating);
        calculateRating();
    }

    public void calculateRating() {
        int ratingsCnt = 0;
        int sum = 0;
        for(Integer currentRating : allRatings) {
            ratingsCnt++;
            sum += currentRating;
        }
        this.rating = sum / (double)ratingsCnt;
    }
}
