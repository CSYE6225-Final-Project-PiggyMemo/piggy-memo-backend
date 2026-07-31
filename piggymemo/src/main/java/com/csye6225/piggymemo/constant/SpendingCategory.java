package com.csye6225.piggymemo.constant;

public enum SpendingCategory {
    HOUSING("HOUSING"),
    TRANSPORTATION("TRANSPORTATION"),
    FOOD("FOOD"),
    DEBTPAYMENTS("DEBTPAYMENTS"),
    UTILITIES("UTILITIES"),
    CLOTHING("CLOTHING"),
    MEDICAL("MEDICAL"),
    OTHER("OTHER");

    private final String record;

    SpendingCategory(String record) {
        this.record = record;
    }

    public String getRecord() {
        return record;
    }
}
