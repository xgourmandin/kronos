package org.kronos.strategy.solving;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.kronos.context.KronosSolvingContext;
import org.kronos.strategy.KronosTestSlotDataBuilder;
import org.kronos.strategy.spacing.NoSpaceSpacingStrategy;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Enable only when benchmarking")
public class BenchmarkKronosConflictDetectionStartegy {

    @ParameterizedTest
    @ValueSource(ints = {1000, 10000, 50000, 100000})
    @DisplayName("Conflict detection strategy - Performance benchmark")
    public void benchmarkSolverAlgorithmPerformance(int slotNumber) {
        KronosConflictDetectionStrategy strategy = new KronosConflictDetectionStrategy();
        SecureRandom rnd = new SecureRandom();
        KronosTestSlotDataBuilder builder = new KronosTestSlotDataBuilder();
        IntStream.rangeClosed(1, slotNumber).forEach(i -> {
            double score = rnd.nextDouble()*5;
            if (rnd.nextDouble() > 0.8) {
                builder.conflictingSlot(score);
            }else {
                builder.notConflictingSlot(score);
            }
        });
        Logger logger = Logger.getLogger("Benchmark");
        final Instant startTime = Instant.now();
        strategy.solve(new KronosSolvingContext().withSlotSpacingStrategy(new NoSpaceSpacingStrategy()), builder.slots());
        final long duration = Duration.between(startTime, Instant.now()).toMillis();
        logger.info("Planning solving took "+ duration + " ms to solve "+slotNumber+ " slots");
        assertTrue(duration < 100);
    }
}
