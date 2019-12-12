package org.kronos.solver.strategy;

import org.kronos.model.KronosSlot;

import java.util.List;

public interface KronosSolverStrategy {

    String getName();

    List<KronosSlot> solve(List<KronosSlot> slots);
}
