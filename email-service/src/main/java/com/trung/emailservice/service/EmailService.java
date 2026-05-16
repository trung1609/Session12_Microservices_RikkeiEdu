package com.trung.emailservice.service;

import com.trung.emailservice.event.OrderCreateEvent;
import com.trung.emailservice.event.ShippingStatusEvent;
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

//    @KafkaListener(topics = "order-events", groupId = "send-mail")
//    public void sendEmailToCustomer(OrderCreateEvent request){
//        log.info("Received order confirmation: {}", request.getUserEmail());
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("trung8d2005@gmail.com"); // địa chỉ gửi
//        message.setTo(request.getUserEmail());           // địa chỉ nhận
//        message.setSubject("Xác nhận đơn hàng");
//        message.setText("Cảm ơn bạn đã đặt hàng! Đơn hàng của bạn đã được xác nhận và đang được xử lý.");
//
//        mailSender.send(message);
//    }

    @KafkaListener(topics = "shipping-events", groupId = "send-order-status")
    public void handleShippingStatusEvent(ShippingStatusEvent event){
        log.info("Received shipping status event: {}", event);
        if ("DELIVERED".equals(event.getStatus())){
            sendCongratulation(event);
        }
    }


    private void sendCongratulation(ShippingStatusEvent event){
        log.info("Sending congratulation email to {}", event.getCustomerEmail());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("trung8d2005@gmail.com");
        message.setTo(event.getCustomerEmail());
        message.setSubject("Đơn hàng " + event.getOrderId() + " đã được giao thành công!");
        message.setText("Xin chào "+ event.getCustomerEmail() + ",\n\n" +
                "Chúng tôi rất vui mừng thông báo rằng đơn hàng của bạn với mã số " + event.getOrderId() + " đã được giao thành công! Cảm ơn bạn đã tin tưởng và sử dụng dịch vụ của chúng tôi.\n\n" +
                "Nếu bạn có bất kỳ câu hỏi nào hoặc cần hỗ trợ thêm, xin đừng ngần ngại liên hệ với chúng tôi.\n\n" +
                "Trân trọng,\n" +
                "Đội ngũ chăm sóc khách hàng");

        log.info("Sending email to {} about order {} delivery status", event.getCustomerEmail(), event.getOrderId());
        mailSender.send(message);
    }
}
