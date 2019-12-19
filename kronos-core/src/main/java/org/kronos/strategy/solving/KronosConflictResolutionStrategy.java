package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.validating.KronosValidationResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KronosConflictResolutionStrategy implements KronosSolverStrategy {
    public static final String NAME = "ConflictResolutionStrategy";
    private KronosSolvingContext solvingContext;

    @Override
    public List<KronosSlot> solve(KronosSolvingContext solvingContext, List<KronosSlot> slots) {
        this.solvingContext = solvingContext;
        slots.sort(Comparator.comparing(KronosSlot::getStart));
        return linearConflictResolution(slots);
    }

    private List<KronosSlot> linearConflictResolution(List<KronosSlot> slots) {
        List<KronosSlot> solvedSlots = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (j < slots.size()) {
            ResolutionResult result = solveNextSlot(slots, i, j);
            i = result.nextIndex;
            solvedSlots.add(result.solvedSlot);
            j++;
        }
        handleLastSlotCase(slots, solvedSlots);
        return solvedSlots;
    }

    private ResolutionResult solveNextSlot(List<KronosSlot> slots, int i, int j) {
        KronosValidationResult validationResult = validateSlot(slots.get(i));
        if (validationResult.validationSucceeded()) {
            return computeNextSlotState(slots.get(i), slots.get(j), j);
        } else {
            final ResolutionResult result = new ResolutionResult();
            result.solvedSlot = slots.get(i).changeStatus(validationResult.getFailureStatus());
            result.nextIndex = ++i;
            return result;
        }
    }

    private KronosValidationResult validateSlot(KronosSlot slot) {
        if (solvingContext.getSlotValidationStrategy().isPresent()) {
            return solvingContext.getSlotValidationStrategy().get().validate(slot);
        } else {
            return KronosValidationResult.success();
        }
    }

    private ResolutionResult computeNextSlotState(KronosSlot slot1, KronosSlot slot2, int slot2Index) {
        final ResolutionResult result = new ResolutionResult();
        if (slotsAreInConflict(slot1, slot2)) {
            if (slot1.getScore() >= slot2.getScore()) {
                result.solvedSlot = slot2.changeStatus(KronosSlotStatus.CONFLICT);
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
    }

    @Override
    public String getName() {
        return NAME;
    }

    private class ResolutionResult {
        public int nextIndex;
        public KronosSlot solvedSlot;
    }
}