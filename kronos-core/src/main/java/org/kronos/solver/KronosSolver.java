package org.kronos.solver;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.solving.KronosConflictDetectionStrategy;
import org.kronos.strategy.solving.KronosSolverStrategy;

import java.util.List;

public class KronosSolver {

    private final KronosSolverStrategy strategy;
    private KronosSolvingContext solvingContext;

    private KronosSolver(KronosSolverStrategy strategy, KronosSolvingContext context) {
        super();
        this.strategy = strategy;
        this.solvingContext = context;
    }

    public static KronosSolver create(KronosSolvingContext context) {
        return new KronosSolver(new KronosConflictDetectionStrategy(), context);
    }

    public static KronosSolver create(KronosSolverStrategy strategy, KronosSolvingContext context) {
        return new KronosSolver(strategy, context);
    }

    public KronosSolverStrategy getSolvingStrategy() {
        return strategy;
    }

    public List<KronosSlot> solve(List<KronosSlot> slots) {
        return strategy.solve(solvingContext, slots);
    }

}
