package org.kronos.strategy.validating;

import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;

import java.util.List;
import java.util.Optional;

public class StatefulAuthorizedSlotsValidationStrategy implements KronosSlotValidationStrategy {
    public static final String NAME = "AuthorizedSlotValidationStrategy";
    private List<KronosSlot> slots;
    private int currentIndex;

    public StatefulAuthorizedSlotsValidationStrategy(List<KronosSlot> slots) {
        super();
        this.slots = slots;
        currentIndex = 0;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Optional<KronosSlotStatus> validate(KronosSlot testedSlot) {
        if (currentIndex == slots.size()) { return Optional.of(KronosSlotStatus.INVALID);}
        final KronosSlot validatingSlot = slots.get(currentIndex);
        if((validatingSlot.getStart().isBefore(testedSlot.getStart()) || validatingSlot.getStart().isEqual(testedSlot.getStart()))
                && (validatingSlot.getEnd().isAfter(testedSlot.getEnd()) || validatingSlot.getEnd().isEqual(testedSlot.getEnd()))) {
            return Optional.empty();
        }
        else {
            if (validatingSlot.getStart().isBefore(testedSlot.getStart())) {
                currentIndex++;
                return validate(testedSlot);
            }else {
                return Optional.of(KronosSlotStatus.INVALID);
            }
        }
    }
}
