package com.learnease.server.util;


import org.springframework.stereotype.Component;

import java.util.Random;


@Component
public class OtpUtil {

    public String generateOtp(int OTP_LENGTH) {
        int min = (int) Math.pow(10, OTP_LENGTH - 1);
        int max = (int) Math.pow(10, OTP_LENGTH) - 1;
        return String.valueOf(new Random().nextInt(max - min + 1) + min);
    }
}
