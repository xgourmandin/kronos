package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class KronosConflictDetectionStrategy implements KronosSolverStrategy {
    public static final String NAME = "CONFLICT_DETECTION";
    private KronosSolvingContext solvingContext;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<KronosSlot> solve(KronosSolvingContext solvingContext, List<KronosSlot> slots) {
        this.solvingContext = solvingContext;
        slots.sort(Comparator.comparing(KronosSlot::getStart));
        return linearConflictDetection(slots);
    }

    private List<KronosSlot> linearConflictDetection(List<KronosSlot> slots) {
        List<KronosSlot> solvedSlots = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (j < slots.size()) {
            Optional<KronosSlotStatus> validationResult = validateSlot(slots.get(i));
            if (validationResult.isPresent()) {
                solvedSlots.add(slots.get(i).changeStatus(validationResult.get()));
                i++;
            } else {
                if (slotsAreInConflict(slots.get(i), slots.get(j))) {
                    if (slots.get(i).getScore() >= slots.get(j).getScore()) {
                        solvedSlots.add(slots.get(j).changeStatus(KronosSlotStatus.CONFLICT));
                    } else {
                        solvedSlots.add(slots.get(i).changeStatus(KronosSlotStatus.CONFLICT));
                        i = j;
                    }
                    j++;
                } else {
                    solvedSlots.add(slots.get(i).changeStatus(KronosSlotStatus.BOOKED));
                    i = j;
                    j++;
                }
            }
        }
        if (solvedSlots.size() == slots.size() - 1) {
            solvedSlots.add(slots.get(i).changeStatus(KronosSlotStatus.BOOKED));
        }
        return solvedSlots;
    }

    private Optional<KronosSlotStatus> validateSlot(KronosSlot slot) {
        if (solvingContext.getSlotValidationStrategy().isPresent()) {
            return solvingContext.getSlotValidationStrategy().get().validate(slot);
        } else {
            return Optional.empty();
        }
    }

    private boolean slotsAreInConflict(KronosSlot slot1, KronosSlot slot2) {
        boolean intersect = slot1.intersect(slot2);
        if (!intersect && solvingContext.getSlotSpacingStrategy().isPresent()) {
            intersect = solvingContext.getSlotSpacingStrategy().get().isSpacingNotEnought(slot1, slot2);
        }
        return intersect;
    }
}
