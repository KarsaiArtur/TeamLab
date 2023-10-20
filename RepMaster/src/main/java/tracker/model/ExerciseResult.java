package tracker.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseResult {
    private int id;
    private int exercise_id;
    private LocalDate date;
    private int set_count;
    private Set []sets;

    public ExerciseResult(int set_count, int exercise_id){
        this.set_count=set_count;
        this.exercise_id=exercise_id;
        sets = new Set[set_count];
    }

    public void addResult(int index, Set result){
        sets[index]=result;
    }
}
