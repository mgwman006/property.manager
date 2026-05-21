package tz.tante.rent.manager.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tz.tante.rent.manager.models.dtos.ApiResponse;
import tz.tante.rent.manager.utilities.Constant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


  @ExceptionHandler(AuthException.class)
  public ResponseEntity<ApiResponse<Object>> handleAuthentication(AuthException exception)
  {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
      .body(ApiResponse.failure(Constant.UNAUTHORIZED_MESSAGE, exception.getMessage(),HttpStatus.UNAUTHORIZED.value()));
  }

  @ExceptionHandler(InitializationException.class)
  public ResponseEntity<ApiResponse<Object>> handleInitializationException(InitializationException exception)
  {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.failure(Constant.BUSINESS_EXCEPTION_MESSAGE,exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
  @ExceptionHandler(TanteException.class)
  public ResponseEntity<ApiResponse<Object>> handleBusiness(TanteException exception)
  {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.failure(Constant.BUSINESS_EXCEPTION_MESSAGE,exception.getMessage(), HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException exception)
  {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(ApiResponse.failure(Constant.RESOURCE_NOT_FOUND_MESSAGE, exception.getMessage(),HttpStatus.NOT_FOUND.value()));
  }

  @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
  public ResponseEntity<Object> handleConflict(ObjectOptimisticLockingFailureException  exception)
  {
    return ResponseEntity.status(HttpStatus.CONFLICT).
      body(ApiResponse.failure(Constant.VERSION_CONFLICT_MESSAGE,exception.getMessage(),HttpStatus.CONFLICT.value()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Object>> handleConstraintViolation(
    ConstraintViolationException ex) {

    String message = ex.getConstraintViolations()
      .stream()
      .map(ConstraintViolation::getMessage)
      .findFirst()
      .orElse("Validation failed");

    return ResponseEntity
      .status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.failure(Constant.VALIDATION_FAILED_MESSAGE,message, HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException exception)
  {
    Map<String, String> errors = new HashMap<>();

    exception.getBindingResult().getFieldErrors()
      .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
      .body(ApiResponse.failure(Constant.VALIDATION_FAILED_MESSAGE, errors,HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception exception)
  {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(ApiResponse.failure(Constant.INTERNAL_SERVER_ERROR_MESSAGE,exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
  }
}
