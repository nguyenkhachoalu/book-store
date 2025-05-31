package com.project_sem4.book_store.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // 1xxx: Validation Errors
    INVALID_KEY(1000, "Khóa không hợp lệ", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1001, "Tên người dùng phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1002, "Mật khẩu phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    FULL_NAME_INVALID(1003, "Tên phải có ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT(1004, "Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT(1005, "Số điện thoại không hợp lệ", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTS(1006, "Email đã được sử dụng", HttpStatus.BAD_REQUEST),
    PHONE_EXISTS(1007, "Số điện thoại đã được sử dụng", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1008, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    INVALID_DATA_FORMAT(1009, "Dữ liệu đầu vào không hợp lệ", HttpStatus.BAD_REQUEST),
    // 1xxx: File Upload
    FILE_EMPTY(1015, "File rỗng", HttpStatus.BAD_REQUEST),
    INVALID_FILE_TYPE(1016, "Loại file không hợp lệ. Chỉ chấp nhận ảnh jpeg/png/webp", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1017, "Tải file thất bại", HttpStatus.INTERNAL_SERVER_ERROR),

    // 1xxx: Book Validation
    BOOK_TITLE_REQUIRED(1010, "Tiêu đề sách không được để trống", HttpStatus.BAD_REQUEST),
    BOOK_AUTHOR_ID_REQUIRED(1011, "Tác giả không được để trống", HttpStatus.BAD_REQUEST),
    BOOK_PRICE_REQUIRED(1012, "Giá sách không được để trống", HttpStatus.BAD_REQUEST),
    BOOK_QUANTITY_REQUIRED(1013, "Số lượng sách không được để trống", HttpStatus.BAD_REQUEST),
    BOOK_CATEGORY_REQUIRED(1014, "Danh mục sách không được để trống", HttpStatus.BAD_REQUEST),

    // 1xxx: Password Validation
    INVALID_PASSWORD(1020, "Mật khẩu hiện tại không đúng", HttpStatus.BAD_REQUEST),
    PASSWORD_SAME_AS_OLD(1021, "Mật khẩu mới không được trùng với mật khẩu cũ", HttpStatus.BAD_REQUEST),

    // 1xxx: Confirm Code Validation
    CONFIRM_CODE_EXPIRED(1030, "Mã xác nhận đã hết hạn hoặc không còn hiệu lực", HttpStatus.BAD_REQUEST),
    CONFIRM_CODE_NOT_FOUND(1031, "Không tìm thấy mã xác nhận", HttpStatus.NOT_FOUND),

    // 1xxx: Category Validation
    CATEGORY_NOT_FOUND(1040, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_EXISTS(1041, "Tên danh mục đã tồn tại", HttpStatus.BAD_REQUEST),

    // 1xxx: Cart Validation
    CART_NOT_FOUND(1050, "Không tìm thấy giỏ hàng", HttpStatus.NOT_FOUND),
    CART_ITEM_ALREADY_EXISTS(1051, "Sản phẩm đã có trong giỏ hàng", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(1052, "Số lượng sản phẩm không hợp lệ", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BOOK_QUANTITY(1053, "Số lượng mua vượt quá tồn kho", HttpStatus.BAD_REQUEST),
    CART_ITEM_NOT_FOUND(1054, "Không tìm thấy sản phẩm trong giỏ hàng", HttpStatus.NOT_FOUND),

    // 1xxx: Wallet Validation
    INVALID_AMOUNT(1060, "Số tiền không hợp lệ", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_BALANCE(1061, "Số dư không đủ", HttpStatus.BAD_REQUEST),
    WALLET_DISABLED(1062, "Ví đã bị vô hiệu hóa", HttpStatus.BAD_REQUEST),

    // 2xxx: Authorization
    UNAUTHENTICATED(2000, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2001, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_TOKEN_SIGNATURE(2002, "Chữ ký token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(2003, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_NOT_FOUND(2004, "Không tìm thấy refresh token", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INACTIVE(2005, "Refresh token đã bị thu hồi", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(2006, "Refresh token đã hết hạn", HttpStatus.UNAUTHORIZED),

    // 3xxx: Entity Not Found
    USER_NOT_EXISTED(3000, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(3001, "Không tìm thấy quyền phù hợp", HttpStatus.BAD_REQUEST),
    ROLE_LIST_EMPTY(3002, "Danh sách quyền không được để trống", HttpStatus.BAD_REQUEST),
    AUTHOR_NOT_FOUND(3003, "Tác giả không tồn tại", HttpStatus.NOT_FOUND),
    BOOK_NOT_FOUND(3004, "Sách không tồn tại", HttpStatus.NOT_FOUND),
    WALLET_NOT_FOUND(3005, "Ví tiền không tồn tại", HttpStatus.NOT_FOUND),
    TRANSACTION_NOT_FOUND(3006, "Giao dịch không tồn tại", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(3007, "Đơn hàng không tồn tại", HttpStatus.NOT_FOUND),
    DELIVERY_NOT_FOUND(3008, "Đơn vận chuyển không tồn tại", HttpStatus.NOT_FOUND),
    SHIPPING_METHOD_NOT_FOUND(3008, "Phương thức vận chuyển không tồn tại", HttpStatus.NOT_FOUND),
    //4xxx: Order Validation
    ORDER_ALREADY_CANCELLED(4000, "Đơn hàng đã bị hủy", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_COMPLETED(4001, "Đơn hàng đã hoàn tất và không thể chỉnh sửa", HttpStatus.BAD_REQUEST),
    ORDER_STATUS_INVALID_TRANSITION(4002, "Không thể chuyển trạng thái đơn hàng", HttpStatus.BAD_REQUEST),
    ORDER_NOT_ELIGIBLE_FOR_CONFIRMATION(4003, "Đơn hàng không đủ điều kiện để xác nhận", HttpStatus.BAD_REQUEST),

    // 4xxx: Delivery Validation
    DELIVERY_CANNOT_BE_MODIFIED(4020, "Chỉ có thể cập nhật đơn giao hàng ở trạng thái PENDING", HttpStatus.BAD_REQUEST),
    DELIVERY_STATUS_CANNOT_BE_CHANGED(4021, "Không thể thay đổi trạng thái khi đơn đã giao hoặc bị hủy", HttpStatus.BAD_REQUEST),

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
