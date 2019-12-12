package org.kronos.solver;

import org.kronos.model.KronosSlot;
import org.kronos.model.KronosSlotStatus;
import org.kronos.model.KronosSlotType;
import org.kronos.solver.strategy.KronosSolverStrategy;

import java.time.LocalDateTime;
import java.util.List;

public class DummyStrategy implements KronosSolverStrategy {
    public static final String DUMMY_STRATEGY = "Dummy-Strategy" ;

    @Override
    public String getName() {
        return DUMMY_STRATEGY;
    }

    @Override
    public List<KronosSlot> solve(List<KronosSlot> slots) {
        KronosSlot slot = KronosSlot.fromPeriod(LocalDateTime.now(), LocalDateTime.now().plusHours(1)).withType(new KronosSlotType() {
            @Override
            public String getName() {
                return "DummySlot";
            }
        }).withStatus(KronosSlotStatus.BOOKED).build();
        slots.add(slot);
        return slots;
    }
}
