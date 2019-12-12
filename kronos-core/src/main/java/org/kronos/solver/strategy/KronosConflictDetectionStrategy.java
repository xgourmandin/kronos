package org.kronos.solver.strategy;

import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KronosConflictDetectionStrategy implements KronosSolverStrategy{
    public static final String NAME = "CONFLICT_DETECTION";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public List<KronosSlot> solve(List<KronosSlot> slots) {
        slots.sort(Comparator.comparing(KronosSlot::getStart));
        return linearConflictDetection(slots);
    }

    private List<KronosSlot> linearConflictDetection(List<KronosSlot> slots) {
        List<KronosSlot> solvedSlots = new ArrayList<>();
        int i = 0;
        int j = 1;
        while (j < slots.size()) {
            if (slots.get(i).intersect(slots.get(j))) {
                solvedSlots.add(slots.get(i).changeStatus(KronosSlotStatus.BOOKED));
                solvedSlots.add(slots.get(j).changeStatus(KronosSlotStatus.CONFLICT));
                j++;
            }
            else {
                solvedSlots.add(slots.get(i).changeStatus(KronosSlotStatus.BOOKED));
                i = j++;
                j++;
            }
        }
        return solvedSlots;
    }
}
