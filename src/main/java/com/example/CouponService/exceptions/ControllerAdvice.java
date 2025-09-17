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

    @ExceptionHandler(CouponUsageOptimisticLockException.class)
    ResponseEntity<Error> handleCouponOptimisticLockException(CouponUsageOptimisticLockException ex) {
        Error error = new Error("COUPON_USAGE_OPTIMISTIC_RETRIES_LIMIT_REACHED", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(UserHasAlreadyUsedCouponException.class)
    ResponseEntity<Error> handleUserHasAlreadyUsedCouponException(UserHasAlreadyUsedCouponException ex) {
        Error error = new Error("USER_HAS_ALREADY_USED_THIS_COUPON", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(ForbiddenCountryException.class)
    ResponseEntity<Error> handleForbiddenCountryException(ForbiddenCountryException ex) {
        Error error = new Error("FORBIDDEN_COUNTRY", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler(IpCheckerException.class)
    ResponseEntity<Error> handleIpCheckerException(IpCheckerException ex) {
        Error error = new Error("IP_CHECKER_ERROR", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(error);
    }
}
