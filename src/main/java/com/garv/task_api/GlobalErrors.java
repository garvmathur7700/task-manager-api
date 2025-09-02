package com.garv.task_api;

import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.Map;


@ResponseStatus(HttpStatus.NOT_FOUND) class NotFound extends RuntimeException { public NotFound(String m){super(m);} }
@ResponseStatus(HttpStatus.FORBIDDEN) class Forbidden extends RuntimeException { public Forbidden(String m){super(m);} }


@RestControllerAdvice
class GlobalErrors {
    @ExceptionHandler({NotFound.class, Forbidden.class})
    public ResponseEntity<Map<String,Object>> known(RuntimeException ex) {
        var status = ex instanceof NotFound ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN;
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", Instant.now().toString(),
                "error", ex.getMessage()
        ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> unk(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "timestamp", Instant.now().toString(),
                "error", "Internal error"
        ));
    }
}
