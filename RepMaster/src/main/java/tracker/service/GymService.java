package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.GymRepository;
import tracker.repository.RatingRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GymService {
    private final GymRepository gymRepository;
    private final WorkoutRepository workoutRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public Gym saveGym(Gym gym){
        return gymRepository.save(gym);
    }

    @Transactional
    public void deleteGym(Gym gym){
        gymRepository.delete(gym);
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

    public void deleteAll(){
        workoutRepository.deleteAllInBatch();
        gymRepository.deleteAllInBatch();
    }
}
