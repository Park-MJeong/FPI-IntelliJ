package com.example.fpi.domain.dto.user;

import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Data
public class CouponListDTO {
    private int couponId;
    private String couponNumber;
    private LocalDateTime couponDate;
    private String state;
    private int discount;
    private String userId;
}
