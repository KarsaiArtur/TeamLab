package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.RegisteredUser;

import java.util.List;

public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer>{
    List<RegisteredUser> findByUserName(String userName);
}
