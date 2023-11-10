package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.GymRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GymService {
    private final GymRepository gymRepository;
    private final WorkoutRepository workoutRepository;

    @Transactional
    public void saveGym(Gym gym){
        gymRepository.save(gym);
    }
    @Transactional
    public void deleteGym(Gym gym){
        gymRepository.delete(gym);
    }
    @Transactional
    public void addNewWorkoutToGym(String id, Workout workout){
        List<Gym> gym = gymRepository.findByName(id);
        workoutRepository.save(workout);
        gym.get(0).addWorkout(workout);
    }
    @Transactional
    public void addExistingWorkoutToGym(String id, String workout_id){
        List<Gym> gym = gymRepository.findByName(id);
        List<Workout> workout = workoutRepository.findByName(workout_id);
        gym.get(0).addWorkout(workout.get(0));
    }
    @Transactional
    public void removeWorkoutFromGym(String id, String workout_id){
        List<Gym> gym = gymRepository.findByName(id);
        List<Workout> workout = workoutRepository.findByName(workout_id);
        gym.get(0).removeWorkout(workout.get(0));
    }

    public void deleteAll(){
        workoutRepository.deleteAllInBatch();
        gymRepository.deleteAllInBatch();
    }



    /*public void setSplitOfGym(int id, Split split){
        List<Gym> gym = gymRepository.findById(id);
        gym.get(0).setSplit(split);
    }

    public void setHowEquipped(int id, Equipment equipped){
        List<Gym> gym = gymRepository.findById(id);
        gym.get(0).setHowEquipped(equipped);
    }*/
}
