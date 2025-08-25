package com.shanzhu.st.service.impl;

import com.shanzhu.st.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    private static class CodeInfo {
        String code;
        long expireTime;
        CodeInfo(String code, long expireTime) {
            this.code = code;
            this.expireTime = expireTime;
        }
    }

    private final Map<String, CodeInfo> cache = new ConcurrentHashMap<>();

    @Override
    public void sendCode(String upi) {
        String code = String.format("%06d", new Random().nextInt(1000000));
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(upi + "@aucklanduni.ac.nz");
        message.setSubject("验证码");
        message.setText("您的验证码是:" + code + "，有效期5分钟");
        mailSender.send(message);
        cache.put(upi, new CodeInfo(code, System.currentTimeMillis() + 5 * 60 * 1000));
    }

    @Override
    public boolean verifyCode(String upi, String code) {
        CodeInfo info = cache.get(upi);
        if (info != null && info.code.equals(code) && info.expireTime > System.currentTimeMillis()) {
            cache.remove(upi);
            return true;
        }
        return false;
    }
}