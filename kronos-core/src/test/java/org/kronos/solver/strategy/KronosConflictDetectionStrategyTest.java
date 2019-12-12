package org.kronos.solver.strategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KronosConflictDetectionStrategyTest {

    @Test
    @DisplayName("Solve a no conflict planning using the conflict detection strategy")
    void solveNoConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(slots);
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Solve a conflicting planning using the conflict detection strategy")
    void solveWithConflict() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().notConflictingSlot().conflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(slots);
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
        assertEquals(4, solved.size());
    }

    @Test
    @DisplayName("Solve a cascading conflicted slots")
    public void testCascadinfConflictDetection() {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().conflictingSlot().conflictingSlot().notConflictingSlot().slots();
        List<KronosSlot> solved = strategy.solve(slots);
        assertEquals(5, solved.size());
        assertEquals(2,solved.stream().filter(s -> KronosSlotStatus.CONFLICT.equals(s.getStatus())).count());
    }
}