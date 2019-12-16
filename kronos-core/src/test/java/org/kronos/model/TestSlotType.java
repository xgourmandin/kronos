package org.kronos.model;

public class TestSlotType extends KronosSlotType {
    public String type = "TestSlotType";

    public TestSlotType() {
        super();
    }

    public TestSlotType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type;
    }
}
