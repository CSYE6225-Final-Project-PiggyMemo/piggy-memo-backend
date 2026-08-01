package com.csye6225.piggymemo.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SpendingCategory {
    HOUSING("Housing"),
    TRANSPORTATION("Transportation"),
    FOOD("Food"),
    DEBTPAYMENTS("Debt payments"),
    UTILITIES("Utilities"),
    CLOTHING("Clothing"),
    MEDICAL("Medical"),
    OTHER("Other");

    private final String record;

    SpendingCategory(String record) {
        this.record = record;
    }

    public String getRecord() {
        return record;
    }

    @JsonCreator
    public static SpendingCategory fromValue(String value) {
        if(value == null) return null;

        for(SpendingCategory c: values()) {
            if(c.name().equalsIgnoreCase(value)) {
                return c;
            }
        }
        
        return OTHER;
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
