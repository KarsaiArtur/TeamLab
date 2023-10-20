package tracker.repository;

import tracker.model.Workout;

public interface WorkoutRepository {
    public Workout findByID(int id);

    public Workout create(Workout w);

    public Workout update(Workout w);

    public boolean delete(Workout w);
}
