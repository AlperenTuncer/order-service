package com.alperentuncer.payment.saga.order_service.api;


public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}