package com.example.fpi.domain.dto.pro;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
public class ProRequestDTO {

    private Long proRequestId;
    private Long proRequestPay;
    private String proRequestContent;
    private LocalDate proRequestDate;
    private String proRequestProgress;
    private Long proId;
    private Long userUploadId;
    private Long checkProReview;
    private Long checkUserReview;
}
