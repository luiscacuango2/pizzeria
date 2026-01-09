package com.luigi.pizza.service.excepcional;

public class EmailApiException extends RuntimeException {
    public EmailApiException() {
        super("Error sending email...");
    }

}
