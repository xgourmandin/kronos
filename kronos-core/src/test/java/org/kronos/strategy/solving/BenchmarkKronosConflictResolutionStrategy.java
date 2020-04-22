package org.kronos.strategy.solving;

import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosTestSlotDataBuilder;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;
import org.openjdk.jmh.annotations.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.IntStream;

public class BenchmarkKronosConflictResolutionStrategy {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 2)
    public void benchmarkSolverAlgorithmPerformance(ExecutionPlan plan) {
        KronosConflictResolutionStrategy strategy = new KronosConflictResolutionStrategy();
        strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), plan.slots);
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({"100000", "500000", "1000000"})
        public int slotNumber;

        public List<KronosSlot> slots;

        @Setup(Level.Trial)
        public void setup() {
            SecureRandom rnd = new SecureRandom();
            KronosTestSlotDataBuilder builder = new KronosTestSlotDataBuilder();
            IntStream.rangeClosed(1, slotNumber).forEach(i -> {
                double score = rnd.nextDouble() * 5;
                if (rnd.nextDouble() > 0.8) {
                    builder.conflictingSlot(score);
                } else {
                    builder.notConflictingSlot(score);
                }
            });
            slots = builder.slots();
        }
    }
}
