package tracker.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public enum MuscleGroup {
    Forearms,
    Biceps,
    Triceps,
    Abdominals,
    Obliques,
    Upper_Back,
    Lower_Back,
    Lats,
    Hamstrings,
    Quadriceps,
    Calves,
    Glutes,
    Lower_Chest,
    Middle_Chest,
    Upper_Chest,
    Anterior_Deltoids,
    Lateral_Deltoids,
    Posterior_Deltoids;

    @Id
    @GeneratedValue
    private int id;

    @ManyToMany(mappedBy = "secondaryMuscleGroups")
    private List<Exercise> secondaryExercises;

    @ManyToMany
    @JoinTable(
            name = "muscle_group_workout_connection",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "muscle_group_id")
    )
    private List<Workout> workouts;

    @OneToMany(mappedBy = "primaryMuscleGroup")
    private List<Exercise> primaryExercises;
    }
