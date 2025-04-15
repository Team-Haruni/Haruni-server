package org.haruni.global.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.haruni.global.exception.entity.RestApiException;
import org.haruni.global.exception.error.CustomErrorCode;
import org.haruni.global.exception.error.ErrorCode;
import org.haruni.global.exception.error.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO 에러 로깅 처리에 HttpServletRequest 에서 uri 가져와서 구체적인 로깅 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ErrorResponse<String>> handleRestApiException(RestApiException e){
        ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<String>>> handleValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<String> errorMessages = new ArrayList<>();

        for (FieldError error : result.getFieldErrors()){
            String errorMessage = "[ " + error.getField() + " ]" +
                    "[ " + error.getDefaultMessage() + " ]" +
                    "[ " + error.getRejectedValue() + " ]";
            errorMessages.add(errorMessage);
        }

        log.error("@Valid Exception occur with below parameter");
        log.error("{}", errorMessages);

        return handleExceptionInternal(CustomErrorCode.INVALID_PARAMS, errorMessages);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse<String>> handleSqlException(DataAccessException e){
        log.error("SQLException occur with {}", e.getMessage());
        return handleExceptionInternal(CustomErrorCode.SQL_EXCEPTION);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<String>> handleException(Exception e){

        ErrorCode errorCode = CustomErrorCode.INTERNAL_SERVER_ERROR;

        log.error("Error occurred with : {}", e.getMessage());
        log.error("{}", (Object) e.getStackTrace());

        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ErrorResponse<String>> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ResponseEntity<ErrorResponse<List<String>>> handleExceptionInternal(ErrorCode errorCode, List<String> message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorResponse<String> makeErrorResponse(ErrorCode errorCode){
        return ErrorResponse.<String>builder()
                .error(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    private ErrorResponse<List<String>> makeErrorResponse(ErrorCode errorCode, List<String> message){
        return ErrorResponse.<List<String>>builder()
                .error(errorCode.getCode())
                .message(message)
                .build();
    }
}
