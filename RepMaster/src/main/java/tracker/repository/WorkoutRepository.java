package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Workout;

import java.util.List;

/**
 * az edzőtervekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtervekkel
 */
public interface WorkoutRepository extends JpaRepository<Workout, Integer> {
}
