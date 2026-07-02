package ec.edu.ups.icc.fundamentos001.core.exceptions.domain;

import org.springframework.http.HttpStatus;

import ec.edu.ups.icc.fundamentos001.core.exceptions.base.ApplicationException;

public class BadRequestException extends ApplicationException {

    public BadRequestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
