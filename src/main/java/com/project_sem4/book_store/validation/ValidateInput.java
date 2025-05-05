package com.project_sem4.book_store.validation;

import java.util.regex.Pattern;

public class ValidateInput {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(\\+?[0-9]{1,3})?([ .-]?[0-9]{3}){2}[ .-]?[0-9]{4}$"
    );

    public static boolean isValidEmail(String email) {
        return email != null && email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}