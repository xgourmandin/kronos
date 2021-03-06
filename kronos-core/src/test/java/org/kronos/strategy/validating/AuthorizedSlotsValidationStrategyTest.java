package org.kronos.strategy.validating;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosTestSlotDataBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizedSlotsValidationStrategyTest {

    @Test
    @DisplayName("Authorized slots validation strategy - creation")
    public void testStrategyCreation() {
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(new ArrayList<>());
        assertEquals(StatefulAuthorizedSlotsValidationStrategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - Authorized slot")
    public void testSlotIsAuthorized() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().slots();
        KronosSlot testedSlot = KronosSlot.builder(slots.get(0)).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        KronosValidationResult result = strategy.validate(testedSlot);
        assertTrue(result.validationSucceeded());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - Not authorized slot")
    public void testSlotIsNotAuthorized() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().slots();
        final KronosSlot testedSlot = KronosSlot.builder().withStart(slots.get(0).getStart().plusMinutes(5)).withEnd(slots.get(0).getEnd().plusMinutes(5)).withType(slots.get(0).getType()).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        KronosValidationResult result = strategy.validate(testedSlot);
        assertFalse(result.validationSucceeded());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-NV-V")
    public void testSuccessiveValidationCase1() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = KronosSlot.builder(slots.get(0)).build();
        final KronosSlot testedSlot2 = KronosSlot.builder().withStart(slots.get(1).getStart().plusMinutes(5)).withEnd(slots.get(1).getEnd().plusMinutes(5)).withType(slots.get(1).getType()).build();
        final KronosSlot testedSlot3 = KronosSlot.builder(slots.get(2)).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertTrue(strategy.validate(testedSlot1).validationSucceeded());
        assertFalse(strategy.validate(testedSlot2).validationSucceeded());
        assertTrue(strategy.validate(testedSlot3).validationSucceeded());
    }


    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-V")
    public void testSuccessiveValidationCase2() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = KronosSlot.builder(slots.get(0)).build();
        final KronosSlot testedSlot2 = KronosSlot.builder(slots.get(1)).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertTrue(strategy.validate(testedSlot1).validationSucceeded());
        assertTrue(strategy.validate(testedSlot2).validationSucceeded());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-NV")
    public void testSuccessiveValidationCase3() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = KronosSlot.builder(slots.get(0)).build();
        final KronosSlot testedSlot2 = KronosSlot.builder().withStart(slots.get(1).getStart().plusMinutes(5)).withEnd(slots.get(1).getEnd().plusMinutes(5)).withType(slots.get(1).getType()).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertTrue(strategy.validate(testedSlot1).validationSucceeded());
        assertFalse(strategy.validate(testedSlot2).validationSucceeded());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation NV-V")
    public void testSuccessiveValidationCase4() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = KronosSlot.builder().withStart(slots.get(0).getStart().plusMinutes(5)).withEnd(slots.get(0).getEnd().plusMinutes(5)).withType(slots.get(0).getType()).build();
        final KronosSlot testedSlot2 = KronosSlot.builder(slots.get(1)).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertFalse(strategy.validate(testedSlot1).validationSucceeded());
        assertTrue(strategy.validate(testedSlot2).validationSucceeded());
    }
}
