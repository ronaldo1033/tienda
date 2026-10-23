package com.tiendaintegral.exception;
import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*;
import java.time.Instant; import java.util.*;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(NotFoundException.class) ResponseEntity<?> notFound(NotFoundException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler(BusinessException.class) ResponseEntity<?> conflict(BusinessException e){return response(HttpStatus.CONFLICT,e.getMessage());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> invalid(MethodArgumentNotValidException e){
  Map<String,String> errors=new LinkedHashMap<>(); e.getBindingResult().getFieldErrors().forEach(x->errors.put(x.getField(),x.getDefaultMessage()));
  return ResponseEntity.badRequest().body(Map.of("timestamp",Instant.now(),"status",400,"error","Solicitud inválida","details",errors));
 }
 @ExceptionHandler(Exception.class) ResponseEntity<?> generic(Exception e){return response(HttpStatus.INTERNAL_SERVER_ERROR,"Ocurrió un error interno.");}
 private ResponseEntity<?> response(HttpStatus s,String m){return ResponseEntity.status(s).body(Map.of("timestamp",Instant.now(),"status",s.value(),"error",s.getReasonPhrase(),"message",m));}
}
