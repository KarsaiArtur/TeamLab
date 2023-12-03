package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.ExerciseResult;

import java.util.List;
/**
 * az eremdényekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő eredményekkel
 */
public interface ExerciseResultRepository extends JpaRepository<ExerciseResult, Integer>{
}
