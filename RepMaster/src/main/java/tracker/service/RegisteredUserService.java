package tracker.service;

import tracker.model.*;
import tracker.repository.RegisteredUserRepository;

public class RegisteredUserService {

    private RegisteredUserRepository registeredUserRepository;

    public void addGymToUser(int id, Gym gym) {
        RegisteredUser registeredUser = registeredUserRepository.findByID(id);
        registeredUser.addGym(gym);
        registeredUser.addWorkoutsFromUsersGyms();
    }

    public void copyWorkoutToUser(int id, Workout workout) {
        RegisteredUser registeredUser = registeredUserRepository.findByID(id);
        registeredUser.addWorkoutToUser(workout);
    }

    public void rateWorkout(int id, Workout workout, int rating) {
        RegisteredUser registeredUser = registeredUserRepository.findByID(id);
        registeredUser.rateWorkout(workout, rating);
    }

}
