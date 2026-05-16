package com.trung.emailservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingStatusEvent {
    private Long orderId;
    private String status;
    private String customerEmail;
}
