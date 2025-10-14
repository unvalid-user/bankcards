package com.example.bankcards.exception;

public class ResourceAlreadyExists extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceAlreadyExists(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Resource '%s' already exists with '%s': '%s'.", resourceName, fieldName, fieldValue));

        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
