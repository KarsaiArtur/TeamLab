package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Split;

public interface SplitRepository extends JpaRepository<Split, Integer> {
}
