package org.kronos.strategy.spacing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalMarginSpacingStrategyTest {

    @Test
    @DisplayName("Global margin spacing strategy - creation")
    public void testGlobalMarginSpacingStrategyCreation() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(0);
        assertEquals(strategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Global margin spacing strategy - No overlapping slots")
    public void testNoOverlappingSlots() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(1000);
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.fromPeriod(LocalDateTime.now(), end).build();
        KronosSlot slot2 = KronosSlot.fromPeriod(end.plusSeconds(2), end.plusMinutes(30)).build();
        assertFalse(strategy.isSpacingNotEnought(slot1, slot2));
    }

    @Test
    @DisplayName("Global margin spacing strategy - Overlapping slots")
    public void testOverlappingSlots() {
        GlobalMarginSpacingStrategy strategy = new GlobalMarginSpacingStrategy(1000);
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.fromPeriod(LocalDateTime.now(), end).build();
        KronosSlot slot2 = KronosSlot.fromPeriod(end.plusNanos((long) 5e+8), end.plusMinutes(30)).build();
        assertTrue(strategy.isSpacingNotEnought(slot1, slot2));
    }
}
