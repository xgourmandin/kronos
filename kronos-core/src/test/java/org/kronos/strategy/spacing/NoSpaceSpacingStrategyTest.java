package org.kronos.strategy.spacing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.ConflictDetectionDataBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NoSpaceSpacingStrategyTest {

    @Test
    @DisplayName("No space spacing strategy - creation")
    public void testNoSpaceStrategyCreation() {
        NoSpaceSpacingStrategy strategy = new NoSpaceSpacingStrategy();
        assertEquals(NoSpaceSpacingStrategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("No space spacing strategy - Overlapping slots")
    public void testWithOverlappingSlots() {
        NoSpaceSpacingStrategy strategy = new NoSpaceSpacingStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().conflictingSlot().slots();
        assertTrue(strategy.isSpacingNotEnought(slots.get(0), slots.get(1)));
    }

    @Test
    @DisplayName("No space spacing strategy - No overlapping slots")
    public void testWithNoOverlappingSlots() {
        NoSpaceSpacingStrategy strategy = new NoSpaceSpacingStrategy();
        List<KronosSlot> slots = new ConflictDetectionDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        assertFalse(strategy.isSpacingNotEnought(slots.get(0), slots.get(1)));
    }
}
