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

    private class DetectionResult {
        public int nextIndex;
        public KronosSlot solvedSlot;
    }

    private List<KronosSlot> linearConflictDetection(List<KronosSlot> slots) {
        List<KronosSlot> solvedSlots = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (j < slots.size()) {
            DetectionResult result = solveNextSlot(slots, i, j);
            i = result.nextIndex;
            solvedSlots.add(result.solvedSlot);
            j++;
        }
        handleLastSlotCase(slots, solvedSlots);
        return solvedSlots;
    }



    private DetectionResult solveNextSlot(List<KronosSlot> slots, int i, int j) {
        Optional<KronosSlotStatus> validationResult = validateSlot(slots.get(i));
        if (validationResult.isPresent()) {
            final DetectionResult result = new DetectionResult();
            result.solvedSlot = slots.get(i).changeStatus(validationResult.get());
            result.nextIndex = ++i;
            return result;
        } else {
            return computeNextSlotState(slots.get(i), slots.get(j), j);
        }
    }

    private Optional<KronosSlotStatus> validateSlot(KronosSlot slot) {
        if (solvingContext.getSlotValidationStrategy().isPresent()) {
            return solvingContext.getSlotValidationStrategy().get().validate(slot);
        } else {
            return Optional.empty();
        }
    }

    private DetectionResult computeNextSlotState(KronosSlot slot1, KronosSlot slot2, int slot2Index) {
        final DetectionResult result = new DetectionResult();
        if (slotsAreInConflict(slot1, slot2)) {
            if (slot1.getScore() >= slot2.getScore()) {
                result.solvedSlot= slot2.changeStatus(KronosSlotStatus.CONFLICT);
            } else {
                result.solvedSlot = slot1.changeStatus(KronosSlotStatus.CONFLICT);
                result.nextIndex = slot2Index;
            }
        } else {
            result.solvedSlot = slot1.changeStatus(KronosSlotStatus.BOOKED);
            result.nextIndex = slot2Index;
        }
        return result;
    }

    private boolean slotsAreInConflict(KronosSlot slot1, KronosSlot slot2) {
        boolean intersect = slot1.intersect(slot2);
        if (!intersect && solvingContext.getSlotSpacingStrategy().isPresent()) {
            intersect = solvingContext.getSlotSpacingStrategy().get().isSpacingNotEnought(slot1, slot2);
        }
        return intersect;
    }

    private void handleLastSlotCase(List<KronosSlot> slots, List<KronosSlot> solvedSlots) {
        if (solvedSlots.size() == slots.size() - 1) {
            solvedSlots.add(slots.get(slots.size()-1).changeStatus(KronosSlotStatus.BOOKED));
        }
    }
}
