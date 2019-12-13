package org.kronos.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KronosSolvingContextTest {

    @Test
    @DisplayName("Solving context creation")
    public void testSolvingContextCreation() {
        KronosSolvingContext context = new KronosSolvingContext().withSlotSpacingStrategy(new TestSlotSpacingStrategy());
        assertTrue(context.getSlotSpacingStrategy().isPresent());
        assertEquals(TestSlotSpacingStrategy.NAME, context.getSlotSpacingStrategy().get().getName());
    }
}
