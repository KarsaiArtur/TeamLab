package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.Gym;
import tracker.model.Workout;
import tracker.repository.GymRepository;
import tracker.repository.RegisteredUserRepository;
import tracker.repository.WorkoutRepository;

/**
 * fejlesztés során tesztelésre használt adatbázist inicializáló osztály
 */
@RequiredArgsConstructor
@Service
public class InitDbService {
    /**
     * az edzőtermekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtermekkel
     */
    private final GymRepository gymRepository;
    /**
     * az edzőtervekhez tartozó repository, ezen keresztül tudunk kommunikálni (CRUD műveletekkel) az adatbázisban lévő edzőtervekkel
     */
    private final WorkoutRepository workoutRepository;

    /**
     * adatbázist inicializálja
     */
    @Transactional
    public void initDb(){
        Workout workout1 = createWorkout("workout1");
        Workout workout2 = createWorkout("workout2");
        Workout workout3 = createWorkout("workout3");

        Gym gym1 = gymRepository.save(Gym.builder().name("gym1").build());
        Gym gym2 = gymRepository.save(Gym.builder().name("gym2").build());

        gym1.addWorkout(workout1);
        gym1.addWorkout(workout2);
        gym2.addWorkout(workout3);
    }

    /**
     * létrehoz egy új edzőtervet és visszatér vele
     * @param name az edzőterv neve
     * @return az új edzőterv
     */
    public Workout createWorkout(String name){
        return workoutRepository.save(Workout.builder().name(name).build());
    }
}
