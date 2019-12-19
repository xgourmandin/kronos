package org.kronos.strategy.solving;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.KronosTestSlotDataBuilder;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;
import org.kronos.strategy.validating.StatefulAuthorizedSlotsValidationStrategy;

import java.util.List;
import java.util.stream.Collectors;

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
        assertEquals(3, solved.size());
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Conflict resolution strategy - planning solving with conflict")
    public void testPlanningSolvingConflict() {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        final List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().notConflictingSlot().slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(3, solved.size());
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Conflict resolution strategy - planning solving with conflict and priority")
    public void testPlanningSolvingConflictWithPriority() {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        final List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot(1).conflictingSlot(2).notConflictingSlot(1).slots();
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext(), slots);
        assertEquals(3, solved.size());
        solved.forEach(s -> assertEquals(KronosSlotStatus.BOOKED, s.getStatus()));
    }

    @Test
    @DisplayName("Conflict resolution strategy - planning solving with conflict")
    public void testPlanningSolvingConflictWithContext() {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        final List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().notConflictingSlot().conflictingSlot().slots();
        final List<KronosSlot> validatingSlots = slots.stream().map(s -> KronosSlot.builder(s).build()).collect(Collectors.toList());
        validatingSlots.remove(2);
        final List<KronosSlot> solved = strategy.solve(new KronosSolvingContext().withSlotValidationStrategy(new StatefulAuthorizedSlotsValidationStrategy(validatingSlots)).withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), slots);
        assertEquals(4, solved.size());
        assertEquals(1, solved.stream().filter(s -> KronosSlotStatus.INVALID.equals(s.getStatus())).count());
    }

}
