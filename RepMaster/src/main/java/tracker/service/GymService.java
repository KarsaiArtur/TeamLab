package tracker.service;

import tracker.model.Equipment;
import tracker.model.Gym;
import tracker.model.Split;
import tracker.model.Workout;
import tracker.repository.GymRepository;

public class GymService {
    private GymRepository gymRepository;

    public void addWorkoutToGym(int id, Workout workout){
        Gym gym = gymRepository.findByID(id);
        gym.addWorkout(workout);
    }

    public void setSplitOfGym(int id, Split split){
        Gym gym = gymRepository.findByID(id);
        gym.setSplit(split);
    }

    public void setHowEquipped(int id, Equipment equipped){
        Gym gym = gymRepository.findByID(id);
        gym.setHowEquipped(equipped);
    }
}
