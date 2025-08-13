package finalProj.dto.parking;

public class ApiResponse<T> {

	private boolean success;
	private String message;
	private T data;

	// 建構子
	public ApiResponse() {
	}

	public ApiResponse(boolean success, String message, T data) {
		this.success = success;
		this.message = message;
		this.data = data;
	}

	// 靜態工廠方法
	public static <T> ApiResponse<T> success(String message, T data) {
		return new ApiResponse<>(true, message, data);
	}

	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, "操作成功", data);
	}

	public static <T> ApiResponse<T> failure(String message) {
		return new ApiResponse<>(false, message, null);
	}

	// Getter & Setter
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
