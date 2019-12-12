package org.kronos.model;

import java.time.LocalDateTime;

public class KronosSlot {

    private final LocalDateTime start;

    private final LocalDateTime end;

    private final KronosSlotType type;

    private final KronosSlotStatus status;

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
            if (status == null) {
                return new KronosSlot(start, end, type);
            } else {
                return new KronosSlot(start, end, type, status);
            }
        }
    }
}
