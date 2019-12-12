package org.kronos.solver.strategy;

import com.sun.deploy.net.MessageHeader;
import org.kronos.model.KronosSlot;
import org.kronos.model.TestSlotType;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConflictDetectionDataBuilder {

    private static final int SLOT_DURATION = 15;
    private LocalDateTime currentDate;
    private final SecureRandom rand ;
    private List<KronosSlot> slots;

    public ConflictDetectionDataBuilder() {
        super();
        currentDate = LocalDateTime.now();
        rand = new SecureRandom();
        slots = new ArrayList<>();
    }

    public ConflictDetectionDataBuilder notConflictingSlot() {
        final LocalDateTime end = currentDate.plusMinutes(5 + SLOT_DURATION);
        final KronosSlot slot = KronosSlot.fromPeriod(currentDate.plusMinutes(5), end).withType(new TestSlotType()).build();
        currentDate = end;
        slots.add(slot);
        return this;
    }

    public ConflictDetectionDataBuilder conflictingSlot() {
        final LocalDateTime start = currentDate.minusMinutes(rand.nextInt((SLOT_DURATION - 5) / 2) + 5);
        final LocalDateTime end = start.plusMinutes(SLOT_DURATION);
        KronosSlot slot = KronosSlot.fromPeriod(start, end).withType(new TestSlotType()).build();
        currentDate = end;
        slots.add(slot);
        return this;
    }

    public List<KronosSlot> slots() {
        return slots;
    }
}