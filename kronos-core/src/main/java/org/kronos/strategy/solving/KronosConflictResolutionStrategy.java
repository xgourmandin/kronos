package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.strategy.validating.KronosValidationResult;

import java.time.LocalDateTime;
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
        for (int i = 0; i < slots.size() - 1; i++) {
            final KronosSlot currentSlot = slots.get(i);
            final KronosValidationResult kronosValidationResult = validateSlot(currentSlot);
            if (kronosValidationResult.validationSucceeded()) {
                int j = i + 1;
                while (j < slots.size()) {
                    final KronosSlot nextSlot = slots.get(j);
                    if (slotsAreInConflict(currentSlot, nextSlot)) {
                        if (currentSlot.getScore() >= nextSlot.getScore()) {
                            final KronosSlot newNextSlot = computeNewNextSlot(currentSlot, nextSlot);
                            slots.set(j, newNextSlot);
                        } else {
                            final KronosSlot newCurrentSlot = computeNewCurrentSlot(currentSlot, nextSlot);
                            slots.set(i, newCurrentSlot);
                            break;
                        }
                    } else {
                        KronosSlotStatus currentSlotStatus;
                        if (currentSlot.getStatus() == null) {
                            currentSlotStatus = KronosSlotStatus.BOOKED;
                        } else {
                            currentSlotStatus = currentSlot.getStatus();
                        }
                        final KronosSlot newCurrentSlot = KronosSlot.builder(currentSlot).withStatus(currentSlotStatus).build();
                        final KronosSlot newNextSlot = KronosSlot.builder(nextSlot).withStatus(KronosSlotStatus.BOOKED).build();
                        slots.set(i, newCurrentSlot);
                        slots.set(j, newNextSlot);
                        break;
                    }
                    j++;
                }
            } else {
                final KronosSlot newCurrentSlot = KronosSlot.builder(currentSlot).withStatus(kronosValidationResult.getFailureStatus()).build();
                slots.set(i, newCurrentSlot);
            }
        }
        return slots;
    }

    private KronosValidationResult validateSlot(KronosSlot slot) {
        if (solvingContext.getSlotValidationStrategy().isPresent()) {
            return solvingContext.getSlotValidationStrategy().get().validate(slot);
        } else {
            return KronosValidationResult.success();
        }
    }

    private KronosSlot computeNewNextSlot(KronosSlot currentSlot, KronosSlot nextSlot) {
        LocalDateTime newStart = currentSlot.getEnd().plusNanos(getSlotSpacingInNano(currentSlot, nextSlot));
        if (newStart.isAfter(nextSlot.getEnd())) {
            newStart = nextSlot.getEnd();
        }
        return KronosSlot.builder(nextSlot).withStart(newStart).withStatus(KronosSlotStatus.REDUCED).build();
    }

    private KronosSlot computeNewCurrentSlot(KronosSlot currentSlot, KronosSlot nextSlot) {
        LocalDateTime newEnd = nextSlot.getStart().minusNanos(getSlotSpacingInNano(currentSlot, nextSlot));
        if (newEnd.isBefore(currentSlot.getStart())) {
            newEnd = currentSlot.getStart();
        }
        return KronosSlot.builder(currentSlot).withEnd(newEnd).withStatus(KronosSlotStatus.REDUCED).build();
    }


    private boolean slotsAreInConflict(KronosSlot slot1, KronosSlot slot2) {
        boolean intersect = slot1.intersect(slot2);
        if (!intersect && solvingContext.getSlotSpacingStrategy().isPresent()) {
            intersect = solvingContext.getSlotSpacingStrategy().get().isSpacingNotEnought(slot1, slot2);
        }
        return intersect;
    }

    private long getSlotSpacingInNano(KronosSlot slot1, KronosSlot slot2) {
        if (solvingContext.getSlotSpacingStrategy().isPresent()) {
            return (long) (solvingContext.getSlotSpacingStrategy().get().getSlotSpacing(slot1, slot2) * 1e+6);
        } else {
            return 0;
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

}
