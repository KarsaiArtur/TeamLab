package tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.GymRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class GymService {
    private GymRepository gymRepository;

    public void addWorkoutToGym(int id, Workout workout){
        Optional<Gym> gym = gymRepository.findById(id);
        gym.get().addWorkout(workout);
    }

    public void setSplitOfGym(int id, Split split){
        Optional<Gym> gym = gymRepository.findById(id);
        gym.get().setSplit(split);
    }

    public void setHowEquipped(int id, Equipment equipped){
        Optional<Gym> gym = gymRepository.findById(id);
        gym.get().setHowEquipped(equipped);
    }
}
