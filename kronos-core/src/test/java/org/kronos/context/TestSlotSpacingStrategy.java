package org.kronos.context;

import org.kronos.model.KronosSlot;
import org.kronos.strategy.spacing.KronosSlotSpacingStrategy;

public class TestSlotSpacingStrategy implements KronosSlotSpacingStrategy {
    public static final String NAME = "TestSlotSpacingStrategy";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSpacingEnought(KronosSlot slot1, KronosSlot slot2) {
        return false;
    }

    @Override
    public long getSlotSpacing(KronosSlot slot1, KronosSlot slot2) {
        return 0;
    }
}
