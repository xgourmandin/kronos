package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;

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
            final KronosSlot nextSlot = slots.get(i + 1);
            if (slotsAreInConflict(currentSlot, nextSlot)) {
                if (currentSlot.getScore() >= nextSlot.getScore()) {
                    final KronosSlot newNextSlot = computeNewNextSlot(currentSlot, nextSlot);
                    slots.set(i + 1, newNextSlot);
                } else {
                    final KronosSlot newCurrentSlot = computeNewCurrentSlot(currentSlot, nextSlot);
                    slots.set(i, newCurrentSlot);
                }
            } else {
                final KronosSlot newCurrentSlot = KronosSlot.builder(currentSlot).withStatus(KronosSlotStatus.BOOKED).build();
                final KronosSlot newNextSlot = KronosSlot.builder(nextSlot).withStatus(KronosSlotStatus.BOOKED).build();
                slots.set(i, newCurrentSlot);
                slots.set(i + 1, newNextSlot);
            }
        }
        return slots;
    }

    private KronosSlot computeNewNextSlot(KronosSlot currentSlot, KronosSlot nextSlot) {
        LocalDateTime newStart = currentSlot.getEnd().plusNanos(getSlotSpacingInNano(currentSlot, nextSlot));
        if (newStart.isAfter(nextSlot.getEnd())) {
            newStart = nextSlot.getEnd();
        }
        return KronosSlot.builder(nextSlot).withStart(newStart).withStatus(KronosSlotStatus.CONFLICT).build();
    }

    private KronosSlot computeNewCurrentSlot(KronosSlot currentSlot, KronosSlot nextSlot) {
        LocalDateTime newEnd = nextSlot.getStart().minusNanos(getSlotSpacingInNano(currentSlot, nextSlot));
        if (newEnd.isBefore(currentSlot.getStart())) {
            newEnd = currentSlot.getStart();
        }
        return KronosSlot.builder(currentSlot).withEnd(newEnd).withStatus(KronosSlotStatus.CONFLICT).build();
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
