package org.kronos.strategy.solving;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kronos.context.KronosSolvingContext;
import org.kronos.model.KronosSlot;
import org.kronos.strategy.KronosTestSlotDataBuilder;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;
import org.openjdk.jmh.annotations.*;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@Disabled("Enable only when benchmarking")
public class BenchmarkKronosConflictDetectionStartegy {

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 1, warmups = 2)
    public void benchmarkSolverAlgorithmPerformance(ExecutionPlan plan) {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), plan.slots);
    }

    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({"1000", "10000", "50000", "100000", "500000", "1000000"})
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
