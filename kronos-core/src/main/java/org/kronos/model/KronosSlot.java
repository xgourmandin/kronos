package org.kronos.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class KronosSlot {

    private LocalDateTime start;

    private LocalDateTime end;

    private KronosSlotType type;

    private KronosSlotStatus status;

    private double score;

    public static KronosSlotBuilder builder() {
        return new KronosSlotBuilder();
    }

    public static KronosSlotBuilder builder(KronosSlot slot) {
        return new KronosSlotBuilder().fromSlot(slot);
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
        return builder(this).withStatus(newStatus).build();
    }

    public static class KronosSlotBuilder {
        private LocalDateTime start;
        private LocalDateTime end;
        private KronosSlotType type;
        private KronosSlotStatus status;
        private double score;

        public KronosSlotBuilder fromSlot(KronosSlot slot) {
            start = slot.start;
            end = slot.end;
            type = slot.type;
            status = slot.status;
            score = slot.score;
            return this;
        }

        public KronosSlotBuilder withStart(LocalDateTime start){
            this.start = start;
            return this;
        }

        public KronosSlotBuilder withEnd(LocalDateTime end){
            this.end = end;
            return this;
        }

        public KronosSlotBuilder withType(KronosSlotType type) {
            this.type = type;
            return this;
        }

        public KronosSlotBuilder withStatus(KronosSlotStatus status) {
            this.status = status;
            return this;
        }

        public KronosSlotBuilder withScore(double score) {
            this.score = score;
            return this;
        }

        public KronosSlot build() {
            validate();
            KronosSlot slot = new KronosSlot();
            slot.start = start;
            slot.end = end;
            slot.type = type;
            slot.status = status;
            slot.score = score;
            return slot;
        }

        private void validate() {
            Objects.requireNonNull(start, "Start date of a slot cannot be null");
            Objects.requireNonNull(end, "End date of a slot cannot be null");
            Objects.requireNonNull(type, "Type of a slot cannot be null");
            if (end.isBefore(start)) {
                throw new IllegalArgumentException(String.format("End date of a slot (%s) cannot be before its start date (%s)", end, start));
            }
        }
    }

}
