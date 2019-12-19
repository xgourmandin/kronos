package org.kronos.strategy.spacing;

import org.kronos.model.KronosSlot;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class SlotTypeDependentSpacingStrategy implements KronosSlotSpacingStrategy {

    public static final String NAME = "SlotTypeDependentSpacingStrategy";
    private final Map<String, Map<String, Long>> margins;

    public SlotTypeDependentSpacingStrategy(Map<String, Map<String, Long>> margins) {
        super();
        this.margins = margins;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isSpacingEnought(KronosSlot slot1, KronosSlot slot2) {
        if (margins.containsKey(slot1.getType().getName()) && margins.get(slot1.getType().getName()).containsKey(slot2.getType().getName())) {
            return Duration.between(slot1.getEnd(), slot2.getStart()).toMillis() > margins.get(slot1.getType().getName()).get(slot2.getType().getName());
        } else {
            throw new IllegalArgumentException("Margin between slot types " + slot1.getType().getName() + " and " + slot2.getType().getName() + " is not configured properly");
        }
    }

    @Override
    public long getSlotSpacing(KronosSlot slot1, KronosSlot slot2) {
        return margins.getOrDefault(slot1.getType().getName(), new HashMap<>()).getOrDefault(slot2.getType().getName(), 0l);
    }
}
