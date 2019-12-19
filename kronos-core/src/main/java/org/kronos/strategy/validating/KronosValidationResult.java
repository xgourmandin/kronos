package org.kronos.strategy.validating;

import org.kronos.model.KronosSlotStatus;

public class KronosValidationResult {

    private final boolean success;

    private final KronosSlotStatus status;

    private KronosValidationResult(boolean success, KronosSlotStatus status) {
        this.success = success;
        this.status = status;
    }

    public static KronosValidationResult success() {
        return new KronosValidationResult(true, null);
    }

    public static KronosValidationResult failure(KronosSlotStatus status) {
        return new KronosValidationResult(false, status);
    }

    public boolean validationSucceeded() {
        return success;
    }

    public KronosSlotStatus getFailureStatus() {
        return status;
    }
}
