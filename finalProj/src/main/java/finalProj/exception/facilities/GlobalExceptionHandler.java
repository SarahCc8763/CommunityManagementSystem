package finalProj.exception.facilities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFound(ResourceNotFoundException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 404);
		error.put("error", "Not Found");
		error.put("message", ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(InsufficientPointsException.class)
	public ResponseEntity<String> handleInsufficientPoints(InsufficientPointsException ex) {
		Map<String, Object> error = new HashMap<>();
		error.put("timestamp", LocalDateTime.now());
		error.put("status", 400);
		error.put("error", "Bad Request");
		error.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}
	
	@ExceptionHandler(ReservationConflictException.class)
	public ResponseEntity<?> handleReservationConflict(ReservationConflictException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", 409);
	    error.put("error", "Conflict");
	    error.put("message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
	}

	@ExceptionHandler(ExceedUserLimitException.class)
	public ResponseEntity<?> handleExceedUserLimit(ExceedUserLimitException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", 400);
	    error.put("error", "Bad Request");
	    error.put("message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(InvalidReservationException.class)
	public ResponseEntity<?> handleInvalidReservation(InvalidReservationException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", 400);
	    error.put("error", "Bad Request");
	    error.put("message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(UnauthorizedOperationException.class)
	public ResponseEntity<?> handleUnauthorizedOperation(UnauthorizedOperationException ex) {
	    Map<String, Object> error = new HashMap<>();
	    error.put("timestamp", LocalDateTime.now());
	    error.put("status", 403);
	    error.put("error", "Forbidden");
	    error.put("message", ex.getMessage());
	    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
	}
}