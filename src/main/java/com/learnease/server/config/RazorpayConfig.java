package com.learnease.server.config;



import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RazorpayConfig {

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;


    /**
     * Creates a RazorpayClient bean used across the application
     * return RazorpayClient authenticated with provided credentials
     * throws RazorpayException if credentials are invalid or missing
     */
    @Bean
    public RazorpayClient razorpayClient() throws RazorpayException{
        return new RazorpayClient(keyId , keySecret);
    }
}
