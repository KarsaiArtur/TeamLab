package tracker.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tracker.model.Exercise;
import tracker.model.Gym;
import tracker.model.Rating;
import tracker.model.Workout;
import tracker.repository.ExerciseRepository;
import tracker.repository.GymRepository;
import tracker.repository.RatingRepository;
import tracker.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

@Builder
@RequiredArgsConstructor
@Service
public class WorkoutService {
    private final WorkoutRepository workoutRepository;
    private final ExerciseRepository exerciseRepository;
    private final RatingRepository ratingRepository;

    @Transactional
    public void saveWorkout(Workout workout){
        List<Workout> workouts = workoutRepository.findByName(workout.getName());
        for(Workout w: workouts)
            workoutRepository.save(workout);
    }

    @Transactional
    public void deleteWorkout(Workout workout){
        workoutRepository.delete(workout);
    }

    public List<Exercise> listExercises(String id){
        List<Workout> workout = workoutRepository.findByName(id);
        return workout.get(0).getExercises();
    }



}
