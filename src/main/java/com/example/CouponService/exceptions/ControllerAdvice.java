package com.example.CouponService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(CouponAlreadyExistException.class)
    public ResponseEntity<Error> handleCouponAlreadyException(CouponAlreadyExistException ex) {
        Error error = new Error(ex.getMessage(), "COUPON_ALREADY_EXIST");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    private record Error(String message, String errorCode){}
}
