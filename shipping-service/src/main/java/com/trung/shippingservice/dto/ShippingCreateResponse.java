package com.trung.shippingservice.dto;

import com.trung.shippingservice.constant.ShippingStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippingCreateResponse {
    private Long shipmentId;
    private ShippingStatus status;
    private BigDecimal shippingFee;
}
