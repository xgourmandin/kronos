package org.kronos.strategy.validating;

import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.KronosStrategy;

import java.util.Optional;

public interface StatefulKronosSlotValidationStrategy extends KronosStrategy {

    Optional<KronosSlotStatus> validate(KronosSlot testedSlot);
}
