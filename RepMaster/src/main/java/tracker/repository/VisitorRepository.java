package tracker.repository;

import tracker.model.Visitor;

import java.util.List;

public interface VisitorRepository {

    public Visitor findByID(int id);
    public List<Visitor> findBynName(String username);
    public Visitor create(Visitor v);
    public Visitor update(Visitor v);
    public boolean delete(Visitor v);
}
