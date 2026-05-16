package com.trung.shippingservice.entity;

import com.trung.shippingservice.constant.ShippingStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    private BigDecimal shippingFee;

    @Enumerated(EnumType.STRING)
    private ShippingStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "shipmentId", cascade = CascadeType.ALL)
    private List<ShippingItem> shippingItems;
}
