package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Rating;
/**
 * az értékelésekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő értékeléssekkel
 */
public interface RatingRepository extends JpaRepository<Rating, Integer> {
}
