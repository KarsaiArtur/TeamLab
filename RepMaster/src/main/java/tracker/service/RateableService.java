package tracker.service;

import tracker.model.Rateable;

import java.util.List;

public interface RateableService {
    public Rateable findById(int id);

    public List<Rateable> getPossibleContainers();

    public void addRateable(int idTo, int id);
}
