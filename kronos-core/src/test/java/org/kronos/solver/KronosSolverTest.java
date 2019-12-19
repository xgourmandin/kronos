package org.kronos.solver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.solving.KronosConflictDetectionStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class KronosSolverTest {

    @Test
    @DisplayName("Kronos solver creation test")
    public void testSolverCreation(){
        KronosSolver solver = KronosSolver.create(new KronosSolvingContext());
        assertEquals(KronosConflictDetectionStrategy.NAME, solver.getSolvingStrategy().getName());
    }

    @Test
    @DisplayName("Launch a solving strategy")
    public void testLaunchSolving() {
        KronosSolver solver = KronosSolver.create(new DummyStrategy(), new KronosSolvingContext());
        List<KronosSlot> slots = new ArrayList<>();
        final List<KronosSlot> solvedSlots = solver.solve(slots);
        assertEquals(DummyStrategy.DUMMY_STRATEGY, solver.getSolvingStrategy().getName());
        assertFalse(solvedSlots.isEmpty());
        assertEquals("DummySlot", solvedSlots.get(0).getType().getName());
    }
}
