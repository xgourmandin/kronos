package org.kronos.strategy.validating;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.KronosTestSlotDataBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizedSlotsValidationStrategyTest {

    @Test
    @DisplayName("Authorized slots validation strategy - creation")
    public void testStrategyCreation() {
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(new ArrayList<>());
        assertEquals(strategy.NAME, strategy.getName());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - Authorized slot")
    public void testSlotIsAuthorized() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().slots();
        KronosSlot testedSlot = slots.get(0).clone();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        Optional<KronosSlotStatus> result =  strategy.validate(testedSlot);
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - Not authorized slot")
    public void testSlotIsNotAuthorized() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().slots();
        final KronosSlot testedSlot = KronosSlot.fromPeriod(slots.get(0).getStart().plusMinutes(5), slots.get(0).getEnd().plusMinutes(5)).withType(slots.get(0).getType()).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        Optional<KronosSlotStatus> result =  strategy.validate(testedSlot);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-NV-V")
    public void testSuccessiveValidationCase1() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = slots.get(0).clone();
        final KronosSlot testedSlot2 = KronosSlot.fromPeriod(slots.get(1).getStart().plusMinutes(5), slots.get(1).getEnd().plusMinutes(5)).withType(slots.get(1).getType()).build();
        final KronosSlot testedSlot3 = slots.get(2).clone();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertFalse(strategy.validate(testedSlot1).isPresent());
        assertTrue(strategy.validate(testedSlot2).isPresent());
        assertFalse(strategy.validate(testedSlot3).isPresent());
    }


    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-V")
    public void testSuccessiveValidationCase2() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = slots.get(0).clone();
        final KronosSlot testedSlot2 = slots.get(1).clone();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertFalse(strategy.validate(testedSlot1).isPresent());
        assertFalse(strategy.validate(testedSlot2).isPresent());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation V-NV")
    public void testSuccessiveValidationCase3() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = slots.get(0).clone();
        final KronosSlot testedSlot2 = KronosSlot.fromPeriod(slots.get(1).getStart().plusMinutes(5), slots.get(1).getEnd().plusMinutes(5)).withType(slots.get(1).getType()).build();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertFalse(strategy.validate(testedSlot1).isPresent());
        assertTrue(strategy.validate(testedSlot2).isPresent());
    }

    @Test
    @DisplayName("Authorized slots validation strategy - successive slots validation NV-V")
    public void testSuccessiveValidationCase4() {
        List<KronosSlot> slots = new KronosTestSlotDataBuilder().notConflictingSlot().notConflictingSlot().slots();
        final KronosSlot testedSlot1 = KronosSlot.fromPeriod(slots.get(0).getStart().plusMinutes(5), slots.get(0).getEnd().plusMinutes(5)).withType(slots.get(0).getType()).build();
        final KronosSlot testedSlot2 = slots.get(1).clone();
        StatefulAuthorizedSlotsValidationStrategy strategy = new StatefulAuthorizedSlotsValidationStrategy(slots);
        assertTrue(strategy.validate(testedSlot1).isPresent());
        assertFalse(strategy.validate(testedSlot2).isPresent());
    }
}
