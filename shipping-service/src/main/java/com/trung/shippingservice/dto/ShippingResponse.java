package com.trung.shippingservice.dto;

import com.trung.shippingservice.constant.ShippingStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingResponse {
    private Long shipmentId;
    private Long orderId;
    private String carrier;
    private ShippingStatus status;
}
