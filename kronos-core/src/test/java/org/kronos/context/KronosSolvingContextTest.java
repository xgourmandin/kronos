package org.kronos.context;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class KronosSolvingContextTest {

    @Test
    @DisplayName("Solving context creation")
    public void testSolvingContextCreation() {
        KronosSolvingContext context = new KronosSolvingContext().withSlotSpacingStrategy(new TestSlotSpacingStrategy())
                .withSlotValidationStrategy(new TestSlotValidationStrategy());
        assertTrue(context.getSlotSpacingStrategy().isPresent());
        assertEquals(TestSlotSpacingStrategy.NAME, context.getSlotSpacingStrategy().get().getName());
        assertTrue(context.getSlotValidationStrategy().isPresent());
        assertEquals(TestSlotValidationStrategy.NAME, context.getSlotValidationStrategy().get().getName());
    }
}
