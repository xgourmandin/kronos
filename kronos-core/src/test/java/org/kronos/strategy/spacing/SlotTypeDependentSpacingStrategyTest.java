package org.kronos.strategy.spacing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.model.TestSlotType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SlotTypeDependentSpacingStrategyTest {

    private static final String TYPE_1 = "SlotType1";
    private static final String TYPE_2 = "SlotType2";

    @Test
    @DisplayName("Slot type dependent spacing strategy - creation")
    public void testSlotTypeDependentSpacingStrategyCreation() {
        SlotTypeDependentSpacingStrategy strategy = new SlotTypeDependentSpacingStrategy(new HashMap<>());
        assertEquals(strategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Slot type dependent spacing strategy - No overlapping slots")
    public void testNoOverlappingSlots() {
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.fromPeriod(LocalDateTime.now(), end).withType(new TestSlotType(TYPE_1)).build();
        KronosSlot slot2 = KronosSlot.fromPeriod(end.plusSeconds(2), end.plusMinutes(30)).withType(new TestSlotType(TYPE_2)).build();
        Map<String, Map<String, Double>> margins = new HashMap<>();
        Map<String, Double> type1_type2_margin = new HashMap<>();
        type1_type2_margin.put(TYPE_2, 1000d);
        margins.put(TYPE_1, type1_type2_margin);
        SlotTypeDependentSpacingStrategy strategy = new SlotTypeDependentSpacingStrategy(margins);
        assertTrue(strategy.isSpacingEnought(slot1, slot2));
    }

    @Test
    @DisplayName("Slot type dependent spacing strategy - Overlapping slots")
    public void testOverlappingSlots() {
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.fromPeriod(LocalDateTime.now(), end).withType(new TestSlotType(TYPE_1)).build();
        KronosSlot slot2 = KronosSlot.fromPeriod(end.plusNanos((long) 5e+8), end.plusMinutes(30)).withType(new TestSlotType(TYPE_2)).build();
        Map<String, Map<String, Double>> margins = new HashMap<>();
        Map<String, Double> type1_type2_margin = new HashMap<>();
        type1_type2_margin.put(TYPE_2, 1000d);
        margins.put(TYPE_1, type1_type2_margin);
        SlotTypeDependentSpacingStrategy strategy = new SlotTypeDependentSpacingStrategy(margins);
        assertTrue(strategy.isSpacingNotEnought(slot1, slot2));
    }

    @Test
    @DisplayName("Slot type dependent spacing strategy - Bad configuration")
    public void tesBadConfiguration() {
        LocalDateTime end = LocalDateTime.now().plusMinutes(30);
        KronosSlot slot1 = KronosSlot.fromPeriod(LocalDateTime.now(), end).withType(new TestSlotType(TYPE_1)).build();
        KronosSlot slot2 = KronosSlot.fromPeriod(end.plusNanos((long) 5e+8), end.plusMinutes(30)).withType(new TestSlotType(TYPE_2)).build();
        Map<String, Map<String, Double>> margins = new HashMap<>();
        Map<String, Double> type1_type2_margin = new HashMap<>();
        type1_type2_margin.put("BAD_TYPE", 1000d);
        margins.put(TYPE_1, type1_type2_margin);
        SlotTypeDependentSpacingStrategy strategy = new SlotTypeDependentSpacingStrategy(margins);
        assertThrows(IllegalArgumentException.class, () -> strategy.isSpacingNotEnought(slot1, slot2));
    }
}
