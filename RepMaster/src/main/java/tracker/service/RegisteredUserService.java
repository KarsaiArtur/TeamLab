package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.*;

import java.util.*;

@RequiredArgsConstructor
@Service
public class RegisteredUserService {
    private final RegisteredUserRepository registeredUserRepository;
    private final WorkoutRepository workoutRepository;
    private final RatingRepository ratingRepository;
    private final ExerciseRepository exerciseRepository;
    private final GymRepository gymRepository;

    private boolean userNameDoesntExist(String userName){
        return findUserByName(userName) == null;
    }

    private boolean wrongPassword(String userName, String password) {
        return !findUserByName(userName).getPassword().equals(password);
    }

    public RegisteredUser findUserByName(String userName){
        return registeredUserRepository.findByUserName(userName).size()==0 ? null : registeredUserRepository.findByUserName(userName).get(0);
    }

    @Transactional
    public void addNewGymToUser(Gym gym){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        gymRepository.save(gym);
        registeredUser.get().addGym(gym);
    }

    @Transactional
    public String addRegisteredUser(RegisteredUser rUser) {
        if(!userNameDoesntExist(rUser.getUserName()))
            return "Account creation failed: username already exists";
        registeredUserRepository.save(rUser);
        return "Account created, welcome "+rUser.getUserName();
    }

    public String loginUser(String userName, String password) {
        if(userNameDoesntExist(userName))
            return "Login failed: no User with such username";
        if(wrongPassword(userName, password))
            return "Login failed: wrong password";
        TrackerApplication.getInstance().setLoggedInUser(findUserByName(userName));
        return "Login successful as "+userName;
    }

    public void singOutUser(){
        TrackerApplication.getInstance().setLoggedInUser(RegisteredUser.builder().build());
    }

    @Transactional
    public void rate(Rateable rateable, double rating, String comment){
        RegisteredUser rUser = TrackerApplication.getInstance().getLoggedInUser();
        Rating new_rating = Rating.builder().rating(rating).comment(comment).build();

        ratingRepository.save(new_rating);
        rUser.addRating(rateable, new_rating);
    }

    public List<Exercise> SearchExerciseByMuscleGroup(MuscleGroup muscleGroup, String sortMode){
        List<Exercise> exercises = exerciseRepository.findByPrimaryMuscleGroup(muscleGroup);
        if(muscleGroup == null){
            exercises = exerciseRepository.findAll();
        }
        if(sortMode == "RatingDesc"){
            Collections.sort(exercises, new Comparator<Exercise>() {
                @Override
                public int compare(Exercise o1, Exercise o2) {
                    return Rating.calculateRating(o1) > Rating.calculateRating(o2) ? -1 : (Rating.calculateRating(o1) < Rating.calculateRating(o2)) ? 1 : 0;
                }
            });
        }
        return exercises;
    }

    public List<RegisteredUser> listUsers(){
        return registeredUserRepository.findAll();
    }

    public void deleteAll(){
        ratingRepository.deleteAllInBatch();
        registeredUserRepository.deleteAllInBatch();
        workoutRepository.deleteAll();
        exerciseRepository.deleteAllInBatch();
    }
}
