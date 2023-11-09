package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Set;

public interface SetRepository extends JpaRepository<Set, Integer> {
}
