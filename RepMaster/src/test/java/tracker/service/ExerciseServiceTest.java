package tracker.service;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tracker.model.Exercise;
import tracker.repository.ExerciseRepository;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @InjectMocks
    ExerciseService exerciseService;

    @Mock
    ExerciseRepository exerciseRepository;

    @Test
    void testFindPR() throws Exception {
        Exercise exercise = Exercise.builder()
                .name("testExercise")
                .set_count(4)
                .repetition_count(10)
                .build();

        when(exerciseRepository.findByName("testExercise")).thenReturn(
                Arrays.asList(exercise)
        );
    }
}