package org.kronos.solver;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.solving.KronosConflictDetectionStrategy;
import org.kronos.strategy.solving.KronosSolverStrategy;

import java.util.List;

public class KronosSolver {

    private final KronosSolverStrategy strategy;
    private KronosSolvingContext solvingContext;

    private KronosSolver(KronosSolverStrategy strategy) {
        super();
        this.strategy = strategy;
    }

    public static KronosSolver create() {
        return new KronosSolver(new KronosConflictDetectionStrategy());
    }

    public static KronosSolver create(KronosSolverStrategy strategy) {
        return new KronosSolver(strategy);
    }

    public KronosSolverStrategy getSolvingStrategy() {
        return strategy;
    }

    public List<KronosSlot> solve(List<KronosSlot> slots) {
        return strategy.solve(solvingContext, slots);
    }

    public KronosSolver withContext(KronosSolvingContext solvingContext) {
        this.solvingContext = solvingContext;
        return this;
    }
}
