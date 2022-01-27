package com.inventory_system.backend.exception;

import com.inventory_system.backend.dto.response.StandardResponse;
import com.inventory_system.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class CustomExceptionHandler {

    @Autowired
    TokenService tokenService;

    @ExceptionHandler(UnauthorizedException.class)
    public final ResponseEntity<Object> handleUnauthorized(
            UnauthorizedException ex) {
        return new ResponseEntity<>("UNAUTHORIZED", new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<StandardResponse> handleBusiness(
            BusinessException ex) {
        return new ResponseEntity<>(
                new StandardResponse(null, tokenService.getJWTToken(tokenService.getUserNick()), ex.getErrorCode(), ex.getMessage()),
                new HttpHeaders(), HttpStatus.OK);
    }

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    public final ResponseEntity<StandardResponse> handleIntenalServererror(
            BusinessException ex) {
        return new ResponseEntity<>(
                new StandardResponse(null, tokenService.getJWTToken(tokenService.getUserNick()), ex.getErrorCode(), ex.getMessage()),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
