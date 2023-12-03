package tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tracker.model.Set;

/**
 * a szettekhez tartozó repository, ezen az osztályon keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő szettekkel
 */
public interface SetRepository extends JpaRepository<Set, Integer> {
}
