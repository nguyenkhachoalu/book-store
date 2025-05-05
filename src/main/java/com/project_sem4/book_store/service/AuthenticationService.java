package com.project_sem4.book_store.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project_sem4.book_store.dto.handle.handle_email.EmailMessage;
import com.project_sem4.book_store.dto.mapper.UserMapper;
import com.project_sem4.book_store.dto.request.authentication_request.IntrospectRequest;
import com.project_sem4.book_store.dto.request.authentication_request.LoginRequest;
import com.project_sem4.book_store.dto.request.authentication_request.LogoutRequest;
import com.project_sem4.book_store.dto.request.authentication_request.RefreshRequest;
import com.project_sem4.book_store.dto.request.user_request.UserCreateRequest;
import com.project_sem4.book_store.dto.response.AuthenticationResponse;
import com.project_sem4.book_store.dto.response.IntrospectResponse;
import com.project_sem4.book_store.dto.response.UserResponse;
import com.project_sem4.book_store.entity.*;
import com.project_sem4.book_store.exception.AppException;
import com.project_sem4.book_store.exception.ErrorCode;
import com.project_sem4.book_store.repository.*;
import com.project_sem4.book_store.validation.ValidateInput;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    CartRepository cartRepository;
    WalletRepository walletRepository;
    UserRepository userRepository;
    ConfirmEmailRepository confirmEmailRepository;
    UserRoleService userRoleService;
    EmailService emailService;
    UserMapper userMapper;
    RefreshTokenRepository refreshTokenRepository;
    PasswordEncoder passwordEncoder;
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected int VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected int REFRESHABLE_DURATION;

    public UserResponse register(UserCreateRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USER_EXISTED);
            }
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new AppException(ErrorCode.EMAIL_EXISTS);
            }
            if (userRepository.existsByPhone(request.getPhone())) {
                throw new AppException(ErrorCode.PHONE_EXISTS);
            }
            if (!ValidateInput.isValidEmail(request.getEmail())) {
                throw new AppException(ErrorCode.INVALID_EMAIL_FORMAT);
            }
            if (!ValidateInput.isValidPhoneNumber(request.getPhone())) {
                throw new AppException(ErrorCode.INVALID_PHONE_FORMAT);
            }

            User user = User.builder()
                    .fullName(request.getFull_name())
                    .username(request.getUsername().toLowerCase())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isActive(false)
                    .build();
            userRepository.save(user);
            userRoleService.assignRoles(user, List.of("USER"));

            ConfirmEmail confirmEmail = ConfirmEmail.builder()
                    .userId(user.getId())
                    .confirmCode(generateCodeActive())
                    .expiryTime(LocalDateTime.now().plusMinutes(5))
                    .createTime(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .isConfirm(false)
                    .isActive(true)
                    .build();
            confirmEmailRepository.save(confirmEmail);

            String emailContent = emailService.generateConfirmationCodeEmail(confirmEmail.getConfirmCode());
            EmailMessage message = new EmailMessage(List.of(request.getEmail()),
                    "Mã xác nhận của " + user.getUsername(), emailContent);
            emailService.sendEmail(message);

            return userMapper.toUserResponse(user);
        } catch (Exception e) {
            log.error("Register failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public UserResponse forgotPassword(a)
    public String confirmRegisterAccount(String confirmCode) {
        ConfirmEmail code = confirmEmailRepository
                .findByConfirmCode(confirmCode)
                .orElseThrow(() -> new AppException(ErrorCode.CONFIRM_CODE_NOT_FOUND));


        if (Boolean.TRUE.equals(code.getIsConfirm())
                || Boolean.FALSE.equals(code.getIsActive())
                || code.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.CONFIRM_CODE_EXPIRED); // hoặc tạo mã lỗi riêng như CONFIRM_CODE_EXPIRED
        }

        User user = userRepository.findById(code.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setIsActive(true);
        code.setIsConfirm(true);
        code.setIsActive(false);

        Wallet wallet = Wallet.builder()
                .userId(user.getId())
                .currency("VND")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        Cart cart = Cart.builder()
                .userId(user.getId())
                .totalPrice(BigDecimal.ZERO)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .build();

        walletRepository.save(wallet);
        cartRepository.save(cart);
        userRepository.save(user);
        confirmEmailRepository.save(code);

        return "Xác nhận đăng ký thành công";
    }


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        }catch (AppException ex){
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? Date.from(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS))
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verified = signedJWT.verify(verifier);

        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN_SIGNATURE);
        }

        if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        if (isRefresh) {
            var tokenInDb = refreshTokenRepository.findByToken(token)
                    .orElseThrow(() -> new AppException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

            if (!tokenInDb.getIsActive()) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_INACTIVE);
            }

            if (tokenInDb.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.REFRESH_TOKEN_EXPIRED);
            }
        }

        return signedJWT;
    }
    public AuthenticationResponse refreshToken(RefreshRequest request) {
        try {
            var signJWT = verifyToken(request.getToken(), true);
            var username = signJWT.getJWTClaimsSet().getSubject();
            var refreshToken = refreshTokenRepository.findByToken(request.getToken())
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
            if (!refreshToken.getIsActive() || refreshToken.getExpiryTime().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            refreshToken.setIsActive(false);
            refreshTokenRepository.save(refreshToken);
            var user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
            var newToken = generateToken(user);
            return AuthenticationResponse.builder()
                    .accessToken(newToken)
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            log.error("Refresh token failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public AuthenticationResponse login(LoginRequest request) {
        try {
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
            var accessToken = generateToken(user);
            var refreshToken = RefreshToken.builder()
                    .userId(user.getId())
                    .token(UUID.randomUUID().toString())
                    .expiryTime(LocalDateTime.now().plusDays(7))
                    .createTime(LocalDateTime.now())
                    .isActive(true)
                    .build();
            refreshTokenRepository.save(refreshToken);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .authenticated(true)
                    .build();
        } catch (Exception e) {
            log.error("Login failed", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
    public void logout(LogoutRequest request) {
        refreshTokenRepository.findByToken(request.getToken()).ifPresent(token -> {
            token.setIsActive(false);  // Đánh dấu token này là không còn hợp lệ
            refreshTokenRepository.save(token);
        });
    }
    public String generateToken(User user) {
        try {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getUsername())
                    .issuer("hoaludeptrai.com")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user.getId()))
                    .build();
            JWSObject jwsObject = new JWSObject(header, new Payload(jwtClaimsSet.toJSONObject()));
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (Exception ex) {
            log.error("Token generation failed", ex);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    String buildScope(UUID userId){
        StringJoiner stringJoiner = new StringJoiner(" ");
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId); // 👈 thêm hàm này vào repo

        for (UserRole userRole : userRoles) {
            Role role = roleRepository.findById(userRole.getRoleId())
                    .orElse(null);
            if (role != null) {
                stringJoiner.add("ROLE_" + role.getRoleCode());
                // Nếu có permission thì xử lý tiếp ở đây (nếu có bảng Permission)
            }
        }

        return stringJoiner.toString();
    }

    private String generateCodeActive() {
        Random random = new Random();
        int code = random.nextInt(100000);
        return String.format("%06d", code);
    }


}
