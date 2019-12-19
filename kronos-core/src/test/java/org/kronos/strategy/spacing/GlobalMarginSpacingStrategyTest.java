package org.kronos.strategy.spacing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.model.TestSlotType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalMarginSpacingStrategyTest {

    @Test
    @DisplayName("Global margin spacing strategy - creation")
    public void testGlobalMarginSpacingStrategyCreation() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(0);
        assertEquals(GlobalMarginSpacingStrategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Global margin spacing strategy - No overlapping slots")
    public void testNoOverlappingSlots() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(1000);
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(end).withType(new TestSlotType()).build();
        KronosSlot slot2 = KronosSlot.builder().withStart(end.plusSeconds(2)).withEnd(end.plusMinutes(30)).withType(new TestSlotType()).build();
        assertFalse(strategy.isSpacingNotEnought(slot1, slot2));
    }

    @Test
    @DisplayName("Global margin spacing strategy - Overlapping slots")
    public void testOverlappingSlots() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(1000);
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(end).withType(new TestSlotType()).build();
        KronosSlot slot2 = KronosSlot.builder().withStart(end.plusNanos((long) 5e+8)).withEnd(end.plusMinutes(30)).withType(new TestSlotType()).build();
        assertTrue(strategy.isSpacingNotEnought(slot1, slot2));
    }

    @Test
    @DisplayName("Global margin spacing strategy - slot spacing value")
    public void testGetSlotSpacing() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(1000);
        final KronosSlot slot = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(LocalDateTime.now()).withType(new TestSlotType()).build();
        assertEquals(1000, strategy.getSlotSpacing(slot, KronosSlot.builder(slot).build()));
    }
}
