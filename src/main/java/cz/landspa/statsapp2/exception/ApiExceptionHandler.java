package cz.landspa.statsapp2.exception;


import cz.landspa.statsapp2.model.DTO.roster.RosterConflictDTO;

import cz.landspa.statsapp2.model.entity.Roster;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(buildResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String message = "Porušení unikátního omezení.";

        Throwable rootCause = getRootCause(e);
        String rootMessage = rootCause != null ? rootCause.getMessage() : "";

        if (rootMessage != null) {
            if (rootMessage.toLowerCase().contains("uniqueusername")) {
                message = "Uživatelské jméno již existuje.";
            } else if (rootMessage.toLowerCase().contains("uniqueemail")) {
                message = "Email již existuje.";
            }
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildResponse(HttpStatus.CONFLICT, message));
    }

    @ExceptionHandler(RosterConflictException.class)
    public ResponseEntity<RosterConflictDTO> handleRosterConflict(RosterConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getConflict());
    }

    @ExceptionHandler(MissingActiveGoalkeeperException.class)
    public ResponseEntity<List<Roster>> handleMissingActiveGoalkeeperException(MissingActiveGoalkeeperException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getGoalkeepers());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errors.put(err.getField(), err.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "errors", errors
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(HttpServletRequest request, Exception e) throws Exception {
        if (request.getRequestURI().startsWith("/api/")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Neočekávaná chyba."));
        }
        throw e;
    }



    private Map<String, Object> buildResponse(HttpStatus status, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );
    }

    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause == null || cause == throwable) {
            return throwable;
        }
        return getRootCause(cause);
    }



}

