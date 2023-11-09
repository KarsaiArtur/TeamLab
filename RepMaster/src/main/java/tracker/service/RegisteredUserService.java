package tracker.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.RegisteredUserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisteredUserService {

    private RegisteredUserRepository registeredUserRepository;

    public void addGymToUser(int id, Gym gym) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().addGym(gym);
        //registeredUser.get().addWorkoutsFromUsersGyms();
    }

    public void copyWorkoutToUser(int id, Workout workout) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        //registeredUser.get().addWorkoutToUser(workout);
    }

    public void rateWorkout(int id, Workout workout, int rating, String comment) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().rateWorkout(workout, rating, comment);
    }

}
