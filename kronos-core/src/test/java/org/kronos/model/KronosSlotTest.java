package org.kronos.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class KronosSlotTest {

    static Stream<Arguments> validDatePeriod() {
        return Stream.of(
                arguments(LocalDateTime.now(), LocalDateTime.now()
                        .plusHours(1), new TestSlotType()));
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot creation test")
    public void testCreateSlotFromPeriod(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        final KronosSlot slot = KronosSlot.builder().withStart(start).withEnd(end).withType(type).build();
        assertEquals(start, slot.getStart());
        assertEquals(end, slot.getEnd());
        assertNotNull(slot.getType().getName());
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot creation with status")
    public void testCreateSlotWithStatus(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        final KronosSlot slot = KronosSlot.builder().withStart(start).withEnd(end).withType(type).withStatus(KronosSlotStatus.BOOKED).build();
        assertEquals(KronosSlotStatus.BOOKED, slot.getStatus());
    }

    @Test
    @DisplayName("Intersection detection of two slots")
    public void testIntersectionDetectionOnSlots() {
        final KronosSlot slot1 = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(LocalDateTime.now().plusHours(1)).withType(new TestSlotType()).build();
        final KronosSlot slot2 = KronosSlot.builder().withStart(LocalDateTime.now().plusMinutes(30)).withEnd(LocalDateTime.now().plusHours(1).plusMinutes(30)).withType(new TestSlotType()).build();
        assertTrue(slot1.intersect(slot2));
    }

    @Test
    @DisplayName("No Intersection of two slots")
    public void testNoIntersectionOnSlots() {
        final KronosSlot slot1 = KronosSlot.builder().withStart(LocalDateTime.now()).withEnd(LocalDateTime.now().plusHours(1)).withType(new TestSlotType()).build();
        final KronosSlot slot2 = KronosSlot.builder().withStart(LocalDateTime.now().plusHours(2)).withEnd(LocalDateTime.now().plusHours(3)).withType(new TestSlotType()).build();
        assertFalse(slot1.intersect(slot2));
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot status mutation - basic")
    public void testStatusUpdate(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        final KronosSlot slot = KronosSlot.builder().withStart(start).withEnd(end).withType(type).withStatus(KronosSlotStatus.BOOKED).build();
        KronosSlot updatedSlot = slot.changeStatus(KronosSlotStatus.CONFLICT);
        assertEquals(KronosSlotStatus.BOOKED, slot.getStatus());
        assertEquals(KronosSlotStatus.CONFLICT, updatedSlot.getStatus());
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot creation with score")
    public void testSlotCreationWithScore(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        final KronosSlot slot = KronosSlot.builder().withStart(start).withEnd(end).withType(type).withScore(1.2d).build();
        assertEquals(1.2d, slot.getScore());
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot cloning")
    public void testCloneSlot(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        final KronosSlot slot = KronosSlot.builder().withStart(start).withEnd(end).withType(type).withScore(1.2d).build();
        KronosSlot clone = KronosSlot.builder(slot).build();
        assertEquals(1.2d, clone.getScore());
    }

    @ParameterizedTest
    @MethodSource("validDatePeriod")
    @DisplayName("Slot date validation")
    public void testSlotDateValidation(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        assertThrows(IllegalArgumentException.class, () -> KronosSlot.builder().withStart(end).withEnd(start).withType(type).build());
    }

}
