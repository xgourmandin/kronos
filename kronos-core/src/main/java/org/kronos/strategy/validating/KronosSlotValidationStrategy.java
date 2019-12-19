package org.kronos.strategy.validating;

import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosStrategy;

public interface KronosSlotValidationStrategy extends KronosStrategy {

    KronosValidationResult validate(KronosSlot testedSlot);
}
