package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.GymRepository;
import tracker.repository.RatingRepository;
import tracker.repository.RegisteredUserRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GymService  implements RateableService{
    private final GymRepository gymRepository;
    private final WorkoutRepository workoutRepository;
    private final RegisteredUserRepository registeredUserRepository;

    @Transactional
    public Gym saveGym(Gym gym){
        return gymRepository.save(gym);
    }

    @Transactional
    public void deleteGym(int id){
        Optional<Gym> gym = gymRepository.findById(id);
        gymRepository.delete(gym.get());
    }


    public Gym findGym(int id) {
        return gymRepository.findById(id).isEmpty() ? null : gymRepository.findById(id).get();
    }

    public List<Gym> listGyms() {
        return gymRepository.findAll();
    }

    public List<Gym> listUserGyms(){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        return registeredUser.get().getUserGyms();
    }

    @Transactional
    public void addNewWorkoutToGym(int id, Workout workout){
        Optional<Gym> gym = gymRepository.findById(id);
        workoutRepository.save(workout);
        gym.get().addWorkout(workout);
    }

    @Transactional
    public void addExistingWorkoutToGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);
        gym.get().addWorkout(workout.get());
    }

    @Transactional
    public void removeWorkoutFromGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);
        gym.get().removeWorkout(workout.get());
    }

    public List<Workout> listWorkouts(int id){
        Optional<Gym> gym = gymRepository.findById(id);
        return gym.get().getWorkouts();
    }

    @Transactional
    public void deleteAll(){
        gymRepository.deleteAllInBatch();
    }

    @Override
    public Rateable findById(int id) {
        return gymRepository.findById(id).get();
    }

}
