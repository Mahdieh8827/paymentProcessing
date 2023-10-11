package com.model;

public enum ErrorType {
    DATABASE("database"),
    NETWORK("network"),
    OTHER("other");
    private final String value;
    ErrorType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
