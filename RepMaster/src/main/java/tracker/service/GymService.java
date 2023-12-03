package tracker.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tracker.TrackerApplication;
import tracker.model.*;
import tracker.repository.GymRepository;
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
    private final RegisteredUserService registeredUserService;

    @Transactional
    public Gym saveGym(Gym gym){
        return gymRepository.save(gym);
    }

    @Transactional
    public void deleteGym(int id){
        Optional<Gym> gym = gymRepository.findById(id);
        if(gym.get().getRatings() != null) {
            int size = gym.get().getRatings().size();
            for (int i = 0; i < size; i++) {
                registeredUserService.deleteRating(gym.get(), gym.get().getRatings().get(0).getId());
            }
        }
        registeredUserService.removeGymFromUser(id);
        gymRepository.delete(gym.get());
    }

    public List<Rateable> getPossibleContainers(){
        return null;
    }

    public void addRateable(int idTo, int id){
        registeredUserService.addExistingGymToUser(id);
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

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().addWorkout(workout);
            registeredUserService.addExistingWorkoutToUser(workout.getId());
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.addWorkout(workout);
            registeredUserService.addExistingWorkoutToUser(workout.getId());
        }
    }

    @Transactional
    public void addExistingWorkoutToGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().addWorkout(workout.get());
            registeredUserService.addExistingWorkoutToUser(workout_id);
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.addWorkout(workout.get());
            registeredUserService.addExistingWorkoutToUser(workout_id);
        }
    }

    @Transactional
    public void removeWorkoutFromGym(int id, int workout_id){
        Optional<Gym> gym = gymRepository.findById(id);
        Optional<Workout> workout = workoutRepository.findById(workout_id);

        if(gym.get().getOwner().getId() == TrackerApplication.getInstance().getLoggedInUser().getId()) {
            gym.get().removeWorkout(workout.get());
            registeredUserService.removeWorkoutFromUser(workout_id);
        }
        else{
            Gym newGym = gymChanged(gym.get());
            newGym.removeWorkout(workout.get());
            registeredUserService.removeWorkoutFromUser(workout_id);
        }
    }

    @Transactional
    public Gym gymChanged(Gym gym){
        Gym newGym = Gym.builder()
                .owner(TrackerApplication.getInstance().getLoggedInUser())
                .name(gym.getName())
                .location(gym.getLocation())
                .publiclyAvailable(false)
                .howEquipped(gym.getHowEquipped())
                .build();
        newGym.setSplit(Split.builder()
                .name(Split.SplitType.valueOf(gym.getSplit().getName().toString()))
                .numberOfDays(gym.getSplit().getNumberOfDays())
                .gym(newGym)
                .build());

        registeredUserService.addNewGymToUser(newGym);
        for(Workout workouts: gym.getWorkouts()){
            addExistingWorkoutToGym(newGym.getId(), workouts.getId());
        }

        registeredUserService.removeGymFromUser(gym.getId());
        TrackerApplication.getInstance().setCurrentGym(newGym);
        return newGym;
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
