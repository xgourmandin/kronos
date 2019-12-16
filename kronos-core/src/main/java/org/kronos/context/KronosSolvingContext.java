package org.kronos.context;

import org.kronos.strategy.spacing.KronosSlotSpacingStrategy;
import org.kronos.strategy.validating.StatefulKronosSlotValidationStrategy;

import java.util.Optional;

public class KronosSolvingContext {
    private KronosSlotSpacingStrategy spacingStrategy;
    private StatefulKronosSlotValidationStrategy validationStrategy;

    public KronosSolvingContext withSlotSpacingStrategy(KronosSlotSpacingStrategy spacingStrategy) {
        this.spacingStrategy = spacingStrategy;
        return this;
    }

    public Optional<KronosSlotSpacingStrategy> getSlotSpacingStrategy() {
        return Optional.ofNullable(spacingStrategy);
    }

    public Optional<StatefulKronosSlotValidationStrategy> getSlotValidationStrategy() {
        return Optional.ofNullable(validationStrategy);
    }

    public KronosSolvingContext withSlotValidationStrategy(StatefulKronosSlotValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
        return this;
    }
}
