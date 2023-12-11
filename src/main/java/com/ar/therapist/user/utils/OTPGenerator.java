package com.ar.therapist.user.utils;

import java.util.Random;

public class OTPGenerator {

    public static String generateOTP(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("OTP length must be greater than 0");
        }

        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }
}
