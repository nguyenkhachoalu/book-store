package com.project_sem4.book_store.controller;

import com.project_sem4.book_store.dto.request.authentication_request.IntrospectRequest;
import com.project_sem4.book_store.dto.request.authentication_request.LoginRequest;
import com.project_sem4.book_store.dto.request.authentication_request.LogoutRequest;
import com.project_sem4.book_store.dto.request.authentication_request.RefreshRequest;
import com.project_sem4.book_store.dto.request.user_request.ForgotPasswordRequest;
import com.project_sem4.book_store.dto.request.user_request.UserCreateRequest;
import com.project_sem4.book_store.dto.response.ApiResponse;
import com.project_sem4.book_store.dto.response.AuthenticationResponse;
import com.project_sem4.book_store.dto.response.IntrospectResponse;
import com.project_sem4.book_store.dto.response.data_response_user.UserResponse;
import com.project_sem4.book_store.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/register")
    ApiResponse<UserResponse> registerUser(@RequestBody @Valid UserCreateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(authenticationService.register(request))
                .build();
    }
    @PostMapping("/confirm_register")
    public ApiResponse<String> confirmCodeRegister(@RequestParam String confirmCode) {
        return ApiResponse.<String>builder()
                .result(authenticationService.confirmRegisterAccount(confirmCode))
                .build();
    }
    @PostMapping("/forgot_password")
    ApiResponse<UserResponse> forgotPassword(@RequestBody @Valid ForgotPasswordRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(authenticationService.forgotPassword(request))
                .build();
    }
    @PostMapping("/confirm_forgot_password")
    public ApiResponse<String> confirmCodeForgotPassword(@RequestParam String confirmCode) {
        return ApiResponse.<String>builder()
                .result(authenticationService.confirmForgotPassword(confirmCode))
                .build();
    }
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.login(request))
                .build();
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthenticationResponse> refreshToken(@RequestBody RefreshRequest request) throws Exception {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.refreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws Exception {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }
}
