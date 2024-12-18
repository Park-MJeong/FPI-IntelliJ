package com.example.fpi.domain.vo.user;

import com.example.fpi.domain.dto.user.UserRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Getter
@NoArgsConstructor
@ToString
public class UserRequestVO {

    private Long userRequestId;
    private Long userRequestPay;
    private String userRequestContent;
    private LocalDate userRequestDate;
    private String userRequestProgress;
    private String userId;
    private Long proUploadId;
    private Long checkProReview;
    private Long checkUserReview;

    @Builder
    public UserRequestVO(Long userRequestId, Long userRequestPay, String userRequestContent, LocalDate userRequestDate, String userRequestProgress, String userId, Long proUploadId, Long checkProReview, Long checkUserReview) {
        this.userRequestId = userRequestId;
        this.userRequestPay = userRequestPay;
        this.userRequestContent = userRequestContent;
        this.userRequestDate = userRequestDate;
        this.userRequestProgress = userRequestProgress;
        this.userId = userId;
        this.proUploadId = proUploadId;
        this.checkProReview = checkProReview;
        this.checkUserReview = checkUserReview;
    }

    public static UserRequestVO toEntity(UserRequestDTO userRequestDTO) {
        return UserRequestVO.builder().userRequestId(userRequestDTO.getUserRequestId())
                .userRequestPay(userRequestDTO.getUserRequestPay())
                .userRequestContent(userRequestDTO.getUserRequestContent())
                .userRequestDate(userRequestDTO.getUserRequestDate())
                .userRequestProgress(userRequestDTO.getUserRequestProgress())
                .userId(userRequestDTO.getUserId())
                .proUploadId(userRequestDTO.getProUploadId())
                .checkProReview(userRequestDTO.getCheckProReview())
                .checkUserReview(userRequestDTO.getCheckUserReview())
                .build();
    }

}
