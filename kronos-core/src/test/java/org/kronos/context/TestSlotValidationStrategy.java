package org.kronos.context;

import org.kronos.model.KronosSlot;
import org.kronos.strategy.validating.KronosSlotValidationStrategy;
import org.kronos.strategy.validating.KronosValidationResult;

public class TestSlotValidationStrategy implements KronosSlotValidationStrategy {
    public static final String NAME = "TestSlotValidationStrategy";

    @Override
    public KronosValidationResult validate(KronosSlot testedSlot) {
        return KronosValidationResult.success();
    }

    @Override
    public String getName() {
        return NAME;
    }
}
