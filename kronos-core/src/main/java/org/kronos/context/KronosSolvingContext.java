package org.kronos.context;

import java.util.Optional;

public class KronosSolvingContext {
    private KronosSlotSpacingStrategy spacingStrategy;

    public KronosSolvingContext withSlotSpacingStrategy(KronosSlotSpacingStrategy spacingStrategy) {
        this.spacingStrategy = spacingStrategy;
        return this;
    }

    public Optional<KronosSlotSpacingStrategy> getSlotSpacingStrategy() {
        return Optional.ofNullable(spacingStrategy);
    }
}
