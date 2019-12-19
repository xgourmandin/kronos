package org.kronos.context;

import org.kronos.strategy.spacing.KronosSlotSpacingStrategy;
import org.kronos.strategy.validating.KronosSlotValidationStrategy;

import java.util.Optional;

public class KronosSolvingContext {
    private KronosSlotSpacingStrategy spacingStrategy;
    private KronosSlotValidationStrategy validationStrategy;

    public KronosSolvingContext withSlotSpacingStrategy(KronosSlotSpacingStrategy spacingStrategy) {
        this.spacingStrategy = spacingStrategy;
        return this;
    }

    public KronosSolvingContext withSlotValidationStrategy(KronosSlotValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
        return this;
    }

    public Optional<KronosSlotSpacingStrategy> getSlotSpacingStrategy() {
        return Optional.ofNullable(spacingStrategy);
    }

    public Optional<KronosSlotValidationStrategy> getSlotValidationStrategy() {
        return Optional.ofNullable(validationStrategy);
    }
}
