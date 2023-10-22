package tracker.repository;

import tracker.model.RegisteredUser;

import java.util.List;

public interface RegisteredUserRepository {

    public RegisteredUser findByID(int id);
    public List<RegisteredUser> findByName(String usename);
    public RegisteredUser create(RegisteredUser r);
    public RegisteredUser update(RegisteredUser r);
    public boolean delete(RegisteredUser r);
}
