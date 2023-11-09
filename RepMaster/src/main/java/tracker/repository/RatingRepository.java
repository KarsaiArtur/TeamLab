package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
