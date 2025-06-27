package ru.vistar.geoeditor.controller.common;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vistar.geoeditor.core.exception.GeoObjectNotFoundException;

@RestControllerAdvice
public class GeoObjectControllerExceptionHandler {
    @ExceptionHandler(GeoObjectNotFoundException.class)
    public ResponseEntity<ProblemDetail> handle(GeoObjectNotFoundException ex) {
        return ResponseEntity
                .of(ProblemDetail
                        .forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage()))
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handle(MethodArgumentNotValidException ex) {
        var violations = ex.getFieldErrors()
                .stream()
                .map(e ->
                        ConstraintViolation.of(e.getField(), e.getDefaultMessage())
                )
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(ex.getStatusCode());
        problemDetail.setProperty("violations", violations);

        return ResponseEntity
                .of(problemDetail)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handle(ConstraintViolationException ex) {
        var violations = ex.getConstraintViolations().stream()
                .map(e ->
                        ConstraintViolation.of(extractSimplePropertyName(e.getPropertyPath()), e.getMessage())
                )
                .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setProperty("violations", violations);

        return ResponseEntity
                .of(problemDetail)
                .build();
    }

    private String extractSimplePropertyName(Path propertyPath) {
        String name = null;
        for (Path.Node node : propertyPath) {
            name = node.getName();
        }
        return name != null
                ? name
                : propertyPath.toString();
    }
} 