package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosStrategy;

import java.util.List;

public interface KronosSolverStrategy extends KronosStrategy {

    List<KronosSlot> solve(KronosSolvingContext solvingContext, List<KronosSlot> slots);
}
