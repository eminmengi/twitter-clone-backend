package com.workintech.backend.twitter_clone.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bu sınıf uygulama genelindeki hataları yakalar ve düzgün JSON döner.
 * Örneğin RuntimeException fırlatıldığında 400 Bad Request veya 409 Conflict gibi dönebilir.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );

        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation failed");
        body.put("errors", fieldErrors); // ← alan bazlı hatalar

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * (2) GENEL/İŞ KURALI HATALARI
     * Servis katmanında fırlattığımız (ör. "Bu kullanıcı adı zaten kullanılıyor!") gibi durumlar.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", ex.getMessage()); // tek satırlık anlamlı mesaj

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
