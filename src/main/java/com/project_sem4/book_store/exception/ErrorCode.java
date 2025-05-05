package com.project_sem4.book_store.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // 1xxx: Validation Errors
    INVALID_KEY(1000, "Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1002, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1003, "Số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(1004, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT(1005, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1006, "Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1007, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    FULL_NAME_INVALID(1008, "Tên phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),

    // 1xxx: Password Validation
    INVALID_PASSWORD(1010, "Mật khẩu hiện tại không đúng", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME_AS_OLD(1011, "Mật khẩu mới không được trùng với mật khẩu cũ", HttpStatus.BAD_REQUEST),

    // 1xxx: Confirm Code Validation
    CONFIRM_CODE_EXPIRED(1020, "Mã xác nhận đã hết hạn hoặc không còn hiệu lực", HttpStatus.BAD_REQUEST),
    CONFIRM_CODE_NOT_FOUND(1021, "Không tìm thấy mã xác nhận", HttpStatus.NOT_FOUND),

    // 2xxx: Authorization
    UNAUTHENTICATED(2000, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_TOKEN_SIGNATURE(2002, "Chữ ký token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(2003, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND(2004, "Không tìm thấy refresh token", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INACTIVE(2005, "Refresh token đã bị thu hồi", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(2006, "Refresh token đã hết hạn", HttpStatus.UNAUTHORIZED),

    // 3xxx: Entity
    USER_NOT_EXISTED(3000, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(3001, "Không tìm thấy quyền phù hợp", HttpStatus.BAD_REQUEST),
    ROLE_LIST_EMPTY(3002, "Danh sách quyền không được để trống", HttpStatus.BAD_REQUEST),
    // 9999: Uncategorized
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
