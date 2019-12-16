package org.kronos.context;

import org.kronos.model.KronosSlot;

public interface KronosSlotSpacingStrategy {

    String getName();

    boolean isSpacingEnought(KronosSlot slot1, KronosSlot slot2);

    default boolean isSpacingNotEnought(KronosSlot slot1, KronosSlot slot2) {
        return !isSpacingEnought(slot1, slot2);
    }
}
