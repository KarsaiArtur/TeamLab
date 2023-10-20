package tracker.repository;

import tracker.model.Exercise;

public interface ExerciseRepository {

    public Exercise findById(int id);
    public Exercise create(Exercise e);
    public Exercise update(Exercise e);
    public boolean delete(Exercise e);

}
