package org.kronos.context;

import org.kronos.model.KronosSlot;

public interface KronosSlotSpacingStrategy {

    String getName();

    boolean isSpacingNotEnought(KronosSlot slot1, KronosSlot slot2);
}
