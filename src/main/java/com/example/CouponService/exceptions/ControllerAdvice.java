package com.example.CouponService.exceptions;

import com.example.CouponService.commons.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(CouponAlreadyExistsException.class)
    ResponseEntity<Error> handleCouponAlreadyException(CouponAlreadyExistsException ex) {
        Error error = new Error("COUPON_ALREADY_EXIST", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
