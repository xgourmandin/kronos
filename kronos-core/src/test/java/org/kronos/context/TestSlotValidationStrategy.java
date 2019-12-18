package org.kronos.context;

import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.validating.KronosSlotValidationStrategy;

import java.util.Optional;

public class TestSlotValidationStrategy implements KronosSlotValidationStrategy {
    public static final String NAME = "TestSlotValidationStrategy";

    @Override
    public Optional<KronosSlotStatus> validate(KronosSlot testedSlot) {
        return Optional.empty();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
