package com.eduardo.expense_tracker.services.exceptions;

public class ResourceNotFind extends RuntimeException {
    public ResourceNotFind(String message) {
        super(message);
    }
}
