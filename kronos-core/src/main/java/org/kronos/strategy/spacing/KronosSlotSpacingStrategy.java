package org.kronos.strategy.spacing;

import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosStrategy;

public interface KronosSlotSpacingStrategy extends KronosStrategy {

    boolean isSpacingEnought(KronosSlot slot1, KronosSlot slot2);

    default boolean isSpacingNotEnought(KronosSlot slot1, KronosSlot slot2) {
        return !isSpacingEnought(slot1, slot2);
    }

    long getSlotSpacing(KronosSlot slot1, KronosSlot slot2);
}
