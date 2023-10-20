package tracker.repository;

import tracker.model.Exercise;
import tracker.model.ExerciseResult;

public interface ExerciseResultRepository {

    public ExerciseResult findById(int id);
    public ExerciseResult create(ExerciseResult e);
    public ExerciseResult update(ExerciseResult e);
    public boolean delete(ExerciseResult e);

}
