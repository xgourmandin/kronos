package org.kronos.strategy.spacing;

import org.kronos.context.KronosSlotSpacingStrategy;
import org.kronos.model.KronosSlot;

import java.time.Duration;

public class GlobalMarginSpacingStrategy implements KronosSlotSpacingStrategy {
    public static final String NAME = "GlobalMarginSpacingStrategy";
    private int marginMillis;

    public GlobalMarginSpacingStrategy(int marginMillis) {
        super();
        this.marginMillis = marginMillis;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSpacingNotEnought(KronosSlot slot1, KronosSlot slot2) {
        return Duration.between(slot1.getEnd(), slot2.getStart()).toMillis() < marginMillis;
    }
}
