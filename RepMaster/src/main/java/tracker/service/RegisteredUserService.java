package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegisteredUserService {
    private final RegisteredUserRepository registeredUserRepository;
    private final WorkoutRepository workoutRepository;
    private final RatingRepository ratingRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseResultRepository exerciseResultRepository;
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
    public void addExistingGymToUser(int gym_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Gym> gym = gymRepository.findById(gym_id);
        registeredUser.get().addGym(gym.get());
    }

    @Transactional
    public void addExistingWorkoutToUser(int workout_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<Workout> workout = workoutRepository.findById(workout_id);
        registeredUser.get().addWorkout(workout.get());
    }

    @Transactional
    public void addExerciseResultToUser(int exerciseResult_id){
        Optional<RegisteredUser> registeredUser = registeredUserRepository.findById(TrackerApplication.getInstance().getLoggedInUser().getId());
        Optional<ExerciseResult> exerciseResult = exerciseResultRepository.findById(exerciseResult_id);
        registeredUser.get().addExerciseResult(exerciseResult.get());
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
        TrackerApplication.getInstance().setLoggedIn(true);
        return "Login successful as "+userName;
    }

    public void singOutUser(){
        TrackerApplication.getInstance().setLoggedInUser(RegisteredUser.builder().build());
        TrackerApplication.getInstance().setLoggedIn(false);
    }

    @Transactional
    public void rate(Rateable rateable, double rating, String comment){
        RegisteredUser rUser = TrackerApplication.getInstance().getLoggedInUser();
        Rating new_rating = Rating.builder().rating(rating).comment(comment).build();

        ratingRepository.save(new_rating);
        rUser.addRating(rateable, new_rating);
    }

    public List<Rateable> SearchExerciseByMuscleGroup(String muscleGroup, String sortMode){
        List<Exercise> exercises = exerciseRepository.findAll();
        if(muscleGroup != null){
            exercises = exercises.stream().filter(a -> a.getPrimaryMuscleGroup().toString().toUpperCase().contains(muscleGroup.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Exercise e: exercises) {
            rateables.add(e);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }

    public List<Rateable> SearchGymBySplit(String split, String sortMode){
        List<Gym> gyms = gymRepository.findAll();
        if(split != null){
            gyms = gyms.stream().filter(a -> a.getSplit().getName().toString().toUpperCase().contains(split.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Gym g: gyms) {
            rateables.add(g);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }

    public List<Rateable> SearchWorkoutByName(String name, String sortMode){
        List<Workout> workouts = workoutRepository.findAll();
        if(name != "" && name != null){
            workouts = workouts.stream().filter(a -> a.getName().toUpperCase().contains(name.toUpperCase())).collect(Collectors.toList());
        }
        List<Rateable> rateables = new ArrayList<>();
        for (Workout w: workouts) {
            rateables.add(w);
        }
        rateables = sortMode(rateables, sortMode);
        return rateables;
    }

    public List<Rateable> sortMode(List<Rateable> rateable, String sortMode){
        rateable = rateable.stream().filter(a -> a.isPubliclyAvailable()).collect(Collectors.toList());
        if("RatingDesc".equals(sortMode)){
            Collections.sort(rateable,
                    (o1, o2) -> Rating.calculateRating(o1) > Rating.calculateRating(o2) ? -1 : (Rating.calculateRating(o1) < Rating.calculateRating(o2)) ? 1 : 0);
        }
        if("RatingAsc".equals(sortMode)){
            Collections.sort(rateable,
                    (o1, o2) -> Rating.calculateRating(o1) < Rating.calculateRating(o2) ? -1 : (Rating.calculateRating(o1) > Rating.calculateRating(o2)) ? 1 : 0);
        }
        if("NameAsc".equals(sortMode)){
            Collections.sort(rateable,
                    (o1, o2) -> o1.getName().compareTo(o2.getName()));
        }
        if("NameDesc".equals(sortMode)){
            Collections.sort(rateable,
                    (o1, o2) -> o2.getName().compareTo(o1.getName()));
        }
        return rateable;
    }

    public List<RegisteredUser> listUsers(){
        return registeredUserRepository.findAll();
    }

    public void deleteAll(){
        ratingRepository.deleteAllInBatch();
        registeredUserRepository.deleteAllInBatch();
        workoutRepository.deleteAll();
        exerciseRepository.deleteAllInBatch();
        gymRepository.deleteAllInBatch();
    }
}
