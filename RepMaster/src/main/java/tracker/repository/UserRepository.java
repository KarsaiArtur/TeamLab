package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
