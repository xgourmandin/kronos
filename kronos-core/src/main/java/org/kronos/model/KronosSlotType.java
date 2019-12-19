package org.kronos.model;

public abstract class KronosSlotType {

    public abstract String getName();

    @Override
    public String toString() {
        return getName();
    }
}
