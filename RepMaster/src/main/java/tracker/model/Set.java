package tracker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Set{
    private int repetition_count;
    private double weight;

    public Set(int repetition_count, double weight){
        this.repetition_count = repetition_count;
        this.weight = weight;
    }
}