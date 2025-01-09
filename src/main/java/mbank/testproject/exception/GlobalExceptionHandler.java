package mbank.testproject.exception;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import mbank.testproject.model.dto.response.ErrorResponse;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final XmlMapper xmlMapper = new XmlMapper();

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFoundException(UsernameNotFoundException e) {
        return buildXmlResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildXmlResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handlerAccessDeniedException(AccessDeniedException e) {
        return buildXmlResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = "Validation failed: " + e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Invalid request.");
        return buildXmlResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMalformedRequest(HttpMessageNotReadableException e) {
        return buildXmlResponse(HttpStatus.BAD_REQUEST, "Malformed request body: " + e.getMessage());
    }

    private ResponseEntity<String> buildXmlResponse(HttpStatus status, String message) {
        try {
            ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
            String xmlResponse = xmlMapper.writeValueAsString(errorResponse);
            return ResponseEntity.status(status)
                    .header("Content-Type", "application/xml")
                    .body(xmlResponse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/xml")
                    .body("<error><code>500</code><message>Failed to process error response</message></error>");
        }
    }
}
