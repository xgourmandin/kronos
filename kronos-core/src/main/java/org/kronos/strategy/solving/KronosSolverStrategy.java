package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;

import java.util.List;

public interface KronosSolverStrategy {

    String getName();

    List<KronosSlot> solve(KronosSolvingContext solvingContext, List<KronosSlot> slots);
}
