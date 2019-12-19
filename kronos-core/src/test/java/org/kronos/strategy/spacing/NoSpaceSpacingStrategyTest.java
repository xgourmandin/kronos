package org.kronos.strategy.spacing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.model.TestSlotType;
import org.kronos.strategy.KronosTestSlotDataBuilder;

import java.time.LocalDateTime;
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
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().conflictingSlot().slots();
        assertTrue(strategy.isSpacingNotEnought(slots.get(0), slots.get(1)));
    }

    @Test
    @DisplayName("No space spacing strategy - No overlapping slots")
    public void testWithNoOverlappingSlots() {
        NoSpaceSpacingStrategy strategy = new NoSpaceSpacingStrategy();
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        assertFalse(strategy.isSpacingNotEnought(slots.get(0), slots.get(1)));
    }

    @Test
    @DisplayName("No space spacing strategy - slot spacing value")
    public void testGetSlotSpacing() {
        NoSpaceSpacingStrategy strategy = new NoSpaceSpacingStrategy();
        final KronosSlot slot = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(LocalDateTime.now()).withType(new TestSlotType()).build();
        assertEquals(0, strategy.getSlotSpacing(slot, KronosSlot.builder(slot).build()));
    }
}
