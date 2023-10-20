package tracker.repository;

import tracker.model.Gym;

public interface GymRepository {
    public Gym findByID(int id);

    public Gym create(Gym g);

    public Gym update(Gym g);

    public boolean delete(Gym g);
}
