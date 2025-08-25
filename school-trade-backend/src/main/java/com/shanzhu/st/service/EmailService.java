package com.shanzhu.st.service;

public interface EmailService {
    void sendCode(String upi);
    boolean verifyCode(String upi, String code);
}
