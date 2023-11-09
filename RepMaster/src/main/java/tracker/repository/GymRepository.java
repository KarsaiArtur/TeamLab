package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Gym;

public interface GymRepository extends JpaRepository<Gym, Integer>{
}
