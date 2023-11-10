package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import tracker.model.Exercise;
import tracker.model.ExerciseResult;
import tracker.model.Set;
import tracker.repository.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @InjectMocks
    ExerciseService exerciseService;

    @Mock
    ExerciseRepository exerciseRepository;

    List<Set> sets1;
    List<Set> sets2;
    ExerciseResult result1;
    ExerciseResult result2;
    List<ExerciseResult> results;
    Exercise exercise;

    @BeforeEach
    private void initTests() {
        sets1 = new ArrayList<>();
        sets1.add(new Set(10, 70));
        sets1.add(new Set(8, 80));
        sets1.add(new Set(6, 90));
        sets1.add(new Set(1, 120));

        sets2 = new ArrayList<>();
        sets2.add(new Set(15, 50));
        sets2.add(new Set(12, 60));
        sets2.add(new Set(12, 70));
        sets2.add(new Set(10, 70));

        result1 = new ExerciseResult();
        result1.setSets(sets1);

        result2 = new ExerciseResult();
        result2.setSets(sets2);

        results = new ArrayList<>();
        results.add(result1);
        results.add(result2);

        exercise = Exercise.builder().id(20).exerciseResults(results).build();
    }

    @Test
    public void testFindPR() throws Exception {
        int id = exercise.getId();
        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
        double maxWeight = exerciseService.findPR(id);
        assertThat(maxWeight).isEqualTo(120.0);
    }

    @Test
    public void testFindMaxVolume() throws Exception {
        int id = exercise.getId();
        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
        Set maxVolumeSet = exerciseService.findMaxVolume(id);
        double maxVol = 12 * 70;
        assertThat(maxVolumeSet.getVolume()).isEqualTo(maxVol);
    }

    @Test
    public void testFindMaxTotalVolume() throws Exception {
        int id = exercise.getId();
        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
        double maxTotalVolume = exerciseService.findMaxTotalVolume(id);
        assertThat(maxTotalVolume).isEqualTo(3010.0);
    }

    @Test
    public void testFindMaxTotalVolumeExerciseResult() throws Exception {
        int id = exercise.getId();
        when(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise));
        ExerciseResult maxVolumeResult = exerciseService.findMaxTotalVolumeExerciseResult(id);
        assertThat(maxVolumeResult).isEqualTo(result2);
    }
}