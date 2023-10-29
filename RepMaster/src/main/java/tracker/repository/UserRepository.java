package tracker.repository;

import tracker.model.User;

import java.util.List;

public interface UserRepository {

    public User findByID(int id);
    public List<User> findBynName(String username);
    public User create(User u);
    public User update(User u);
    public boolean delete(User u);
}
