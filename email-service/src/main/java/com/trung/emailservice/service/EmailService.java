package com.trung.emailservice.service;

import com.trung.emailservice.event.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @KafkaListener(topics = "order-events", groupId = "send-mail")
    public void sendEmailToCustomer(OrderCreateEvent request){
        log.info("Received order confirmation: {}", request.getUserEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("trung8d2005@gmail.com"); // địa chỉ gửi
        message.setTo(request.getUserEmail());           // địa chỉ nhận
        message.setSubject("Xác nhận đơn hàng");
        message.setText("Cảm ơn bạn đã đặt hàng! Đơn hàng của bạn đã được xác nhận và đang được xử lý.");

        mailSender.send(message);
    }
}
