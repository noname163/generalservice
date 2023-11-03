package com.cepa.generalservice.services.studentService.impl;

import com.cepa.generalservice.data.entities.Combination;
import com.cepa.generalservice.data.entities.StudentTarget;
import com.cepa.generalservice.data.entities.UserInformation;
import com.cepa.generalservice.data.repositories.CombinationRepository;
import com.cepa.generalservice.data.repositories.StudentTargetRepository;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.services.studentService.impl.StudentTargetServiceImpl;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StudentTargetServiceImplTest {

    @Mock
    private StudentTargetRepository studentTargetRepository;

    @Mock
    private CombinationRepository combinationRepository;

    @InjectMocks
    private StudentTargetServiceImpl studentTargetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStudentTargets_Success() {
        // Mock the behavior of combinationRepository
        List<Long> combinationIds = List.of(1L, 2L, 3L);
        Set<Long> idSet = Set.of(1L, 2L, 3L);
        List<Combination> combinations = createCombinations();

        Mockito.when(combinationRepository.findByIdIn(idSet)).thenReturn(Optional.of(combinations));

        // Mock the behavior of studentTargetRepository
        List<StudentTarget> studentTargets = new ArrayList<StudentTarget>();
        when(studentTargetRepository.findByStudentInformation(Mockito.any(UserInformation.class))).thenReturn(studentTargets);

        // Mock the behavior of studentTargetRepository.saveAll
        Mockito.when(studentTargetRepository.saveAll(Mockito.anyList())).thenReturn(createStudentTargets(combinations));

        // Create a sample UserInformation
        UserInformation userInformation = new UserInformation();

        // Perform the createStudentTargets operation
        studentTargetService.createStudentTargets(userInformation, combinationIds);

        // Verify that studentTargetRepository.saveAll was called
        Mockito.verify(studentTargetRepository, Mockito.times(1)).saveAll(Mockito.anyList());
    }

    @Test
    public void testCreateStudentTargets_EmptyCombinations() {
        // Mock the behavior of combinationRepository to return an empty list of
        // combinations
        List<Long> combinationIds = List.of(1L, 2L, 3L);
        Set<Long> idSet = Set.of(1L, 2L, 3L);
        Mockito.when(combinationRepository.findByIdIn(idSet)).thenReturn(Optional.empty());

        // Create a sample UserInformation
        UserInformation userInformation = new UserInformation();

        // Perform the createStudentTargets operation, which should throw a
        // BadRequestException
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> studentTargetService.createStudentTargets(userInformation, combinationIds));

        assertTrue(exception.getMessage().contains("Cannot found combination"));
    }

    @Test
    public void testCreateStudentTargets_SubjectAlreadyExists() {
        // Mock the behavior of combinationRepository to return a list of combinations
        List<Long> combinationIds = List.of(1L, 2L, 3L);
        Set<Long> idSet = Set.of(1L, 2L, 3L);
        List<Combination> combinations = createCombinations();

        Mockito.when(combinationRepository.findByIdIn(idSet)).thenReturn(Optional.of(combinations));

        // Mock the behavior of studentTargetRepository to return a list of existing
        // student targets
        UserInformation userInformation = UserInformation.builder().build();
        Mockito.when(studentTargetRepository.findByStudentInformation(userInformation))
                .thenReturn(createStudentTargets(combinations));

        // Perform the createStudentTargets operation, which should throw a
        // BadRequestException
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> studentTargetService.createStudentTargets(userInformation, combinationIds));

        assertTrue(
                exception.getMessage().contains("Combination " + combinations.get(0).getName() + " already exists."));
    }

    // Helper method to create a list of sample combinations
    private List<Combination> createCombinations() {
        List<Combination> combinations = new ArrayList<>();
        combinations.add(Combination.builder().id(1).name("Combination1").build());
        combinations.add(Combination.builder().id(1).name("Combination2").build());
        combinations.add(Combination.builder().id(1).name("Combination3").build());
        return combinations;
    }

    // Helper method to create a list of sample student targets
    private List<StudentTarget> createStudentTargets(List<Combination> combinations) {
        List<StudentTarget> studentTargets = new ArrayList<>();
        for (Combination combination : combinations) {
            studentTargets.add(
                    StudentTarget.builder()
                            .id(1)
                            .studentInformation(UserInformation.builder().build())
                            .combination(combination).build());
        }
        return studentTargets;
    }
}
