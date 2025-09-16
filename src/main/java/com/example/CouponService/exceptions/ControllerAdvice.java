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

    @ExceptionHandler(CouponDoesNotExistException.class)
    ResponseEntity<Error> handleCouponDoesNotExistException(CouponDoesNotExistException ex) {
        Error error = new Error("COUPON_DOES_NOT_EXIST", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(CouponUsageLimitExceededException.class)
    ResponseEntity<Error> handleCouponUsageLimitExceededException(CouponUsageLimitExceededException ex) {
        Error error = new Error("COUPON_LIMIT_REACHED", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

}
