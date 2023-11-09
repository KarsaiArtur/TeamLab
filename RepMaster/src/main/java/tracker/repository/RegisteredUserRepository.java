package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.RegisteredUser;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer>{
}
