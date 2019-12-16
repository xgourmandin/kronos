package org.kronos.strategy.solving;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.ConflictDetectionDataBuilder;
import org.kronos.strategy.solving.KronosConflictDetectionStrategy;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class KronosConflictDetectionStrategyTest {

    @Test
    @DisplayName("Solve a no conflict planning using the conflict detection strategy")
    void solveNoConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Solve a conflicting planning using the conflict detection strategy")
    void solveWithConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().notConflictingSlot().conflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
        assertEquals(4, solved.size());
    }

    @Test
    @DisplayName("Solve a cascading conflicted slots")
    public void testCascadingConflictDetection() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().conflictingSlot().conflictingSlot().notConflictingSlot().slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(5, solved.size());
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
    }

    @Test
    @DisplayName("Solve a planning with a simple spacing strategy")
    public void testPlanningSolvingWithSpacingStrategy() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().conflictingSlot().conflictingSlot().notConflictingSlot().slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), slots);
        assertEquals(5, solved.size());
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
    }

    @Test
    @DisplayName("Solve a planning with priority slots")
    public void testSolvingPlanningWithPriority() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot(1).conflictingSlot(2).conflictingSlot(1).slots();
        List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), slots);
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
        assertTrue(slots.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).allMatch(s -> s.getScore() == 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {1000, 10000, 50000, 100000})
    @DisplayName("Conflict detection strategy - Performance benchmark")
    public void benchmarkSolverAlgorithmPerforamnce(int slotNumber) {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        SecureRandom rnd = new SecureRandom();
        ConflictDetectionDataBuilder builder = new ConflictDetectionDataBuilder();
        IntStream.rangeClosed(1, slotNumber).forEach(i -> {
           double score = rnd.nextDouble()*5;
            if (rnd.nextDouble() > 0.8) {
                builder.conflictingSlot(score);
            }else {
                builder.notConflictingSlot(score);
            }
        });
        Logger logger = Logger.getLogger("Benchmark");
        final Instant startTime = Instant.now();
        strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), builder.slots());
        final long duration = Duration.between(startTime, Instant.now()).toMillis();
        logger.info("Planning solving took "+ duration + " ms to solve "+slotNumber+ " slots");
        assertTrue(duration < 100);
    }
}