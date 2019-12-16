package org.kronos.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class KronosSlot {

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final KronosSlotType type;

    private KronosSlotStatus status;

    private double score;

    private KronosSlot(LocalDateTime start, LocalDateTime end, KronosSlotType type) {
        super();
        this.start = start;
        this.end = end;
        this.type = type;
        this.status = null;
    }

    private KronosSlot(LocalDateTime start, LocalDateTime end, KronosSlotType type, KronosSlotStatus status) {
        this.start = start;
        this.end = end;
        this.type = type;
        this.status = status;
    }

    public static KronosSlotBuilder fromPeriod(LocalDateTime start, LocalDateTime end) {
        return new KronosSlotBuilder(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public KronosSlotType getType() {
        return type;
    }

    public KronosSlotStatus getStatus() {
        return status;
    }

    public double getScore() {
        return score;
    }

    public boolean intersect(KronosSlot slot2) {
        return start.isBefore(slot2.end) && end.isAfter(slot2.start);
    }

    public KronosSlot changeStatus(KronosSlotStatus newStatus) {
        return new KronosSlot(start, end, type, newStatus);
    }

    public static class KronosSlotBuilder {
        private final LocalDateTime start;
        private final LocalDateTime end;
        private KronosSlotType type;
        private KronosSlotStatus status;
        private double score;

        public KronosSlotBuilder(LocalDateTime start, LocalDateTime end) {

            this.start = start;
            this.end = end;
        }

        public KronosSlotBuilder withType(KronosSlotType type) {
            this.type = type;
            return this;
        }

        public KronosSlotBuilder withStatus(KronosSlotStatus status) {
            this.status = status;
            return this;
        }

        public KronosSlot build() {
            validate();
            KronosSlot slot = new KronosSlot(start, end, type);
            slot.status = status;
            slot.score = score;
            return slot;
        }

        private void validate() {
            Objects.requireNonNull(start, "Start date of a slot cannot be null");
            Objects.requireNonNull(end, "End date of a slot cannot be null");
            Objects.requireNonNull(type, "Type of a slot cannot be null");
        }

        public KronosSlotBuilder withScore(double score) {
            this.score = score;
            return this;
        }
    }

    public KronosSlot clone() {
        return KronosSlot.fromPeriod(start, end).withType(type).withScore(score).withStatus(status).build();
    }
}
