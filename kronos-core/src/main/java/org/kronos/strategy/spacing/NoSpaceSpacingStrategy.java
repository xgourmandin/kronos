package org.kronos.strategy.spacing;

import org.kronos.model.KronosSlot;

public class NoSpaceSpacingStrategy implements KronosSlotSpacingStrategy {
    public static final String NAME = "NoSpaceStrategy";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSpacingEnought(KronosSlot slot1, KronosSlot slot2) {
        return !slot1.intersect(slot2);
    }

    @Override
    public long getSlotSpacing(KronosSlot slot1, KronosSlot slot2) {
        return 0;
    }
}
