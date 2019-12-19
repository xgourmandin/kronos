package org.kronos.strategy.solving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.KronosTestSlotDataBuilder;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;
import org.kronos.strategy.validating.StatefulAuthorizedSlotsValidationStrategy;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class KronosConflictDetectionStrategyTest {

    @Test
    @DisplayName("Solve a no conflict planning using the conflict detection strategy")
    void solveNoConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Solve a conflicting planning using the conflict detection strategy")
    void solveWithConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().notConflictingSlot().conflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
        assertEquals(4, solved.size());
    }

    @Test
    @DisplayName("Solve a cascading conflicted slots")
    public void testCascadingConflictDetection() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().conflictingSlot().conflictingSlot().notConflictingSlot().slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(5, solved.size());
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
    }

    @Test
    @DisplayName("Solve a planning with a simple spacing strategy")
    public void testPlanningSolvingWithSpacingStrategy() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().conflictingSlot().conflictingSlot().notConflictingSlot().slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), slots);
        assertEquals(5, solved.size());
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
    }

    @Test
    @DisplayName("Solve a planning with priority slots")
    public void testSolvingPlanningWithPriority() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot(1).conflictingSlot(2).conflictingSlot(1).slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), slots);
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
        assertTrue(slots.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).allMatch(s -> s.getScore() == 1));
    }

    @Test
    @DisplayName("Solve a planning with validation strategy")
    public void testSolvingPlanningWithValidation() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> validatingSlots = slots.stream().map(s -> KronosSlot.builder(s).build()).collect(Collectors.toList());
        validatingSlots.remove(2);
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotValidationStrategy(new StatefulAuthorizedSlotsValidationStrategy(validatingSlots)), slots);
        assertEquals(1,solved.stream().filter(s -> KronosSlotStatus.INVALID.equals(s.getStatus())).count());
    }

}