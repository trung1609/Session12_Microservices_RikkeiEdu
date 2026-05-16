package com.trung.shippingservice.mapper;

import com.trung.shippingservice.dto.ShippingResponse;
import com.trung.shippingservice.entity.Shipment;

public class ShippingMapper {
    public static ShippingResponse toDTO(Shipment shipment) {
        return ShippingResponse.builder()
                .shipmentId(shipment.getId())
                .orderId(shipment.getOrderId())
                .carrier(shipment.getCarrier().getName())
                .status(shipment.getStatus())
                .build();
    }
}
