package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.model.*;
import tracker.repository.RatingRepository;
import tracker.repository.RegisteredUserRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RegisteredUserService {

    private RegisteredUserRepository registeredUserRepository;
    private RatingRepository ratingRepository;

    public void addGymToUser(int id, Gym gym) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().addGym(gym);
        //registeredUser.get().addWorkoutsFromUsersGyms();
    }

    /*public void rateWorkout(int id, Workout workout, int rating, String comment) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().rateWorkout(workout, rating, comment);
    }

    public void rateGym(int id, Gym gym, int rating, String comment) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().rateGym(gym, rating, comment);
    }
    public void rateExercise(int id, Exercise exercise, int rating, String comment) {
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(id);
        registeredUser.get().rateExercise(exercise, rating, comment);
    }*/

}
