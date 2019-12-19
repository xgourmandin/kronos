package org.kronos.strategy.solving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.KronosTestSlotDataBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KronosConflictResolutionStrategyTest {

    @Test
    @DisplayName("Conflict resolution strategy - creation")
    public void testStrategyCreation() {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        assertEquals(KronosConflictResolutionStrategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Conflict resolution strategy - planning solving with no conflict")
    public void testPlanningSolvingNoConflict() {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        final List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

}
