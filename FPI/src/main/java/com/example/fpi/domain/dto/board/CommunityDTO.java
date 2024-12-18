package com.example.fpi.domain.dto.board;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Data
public class CommunityDTO {

    private Long communityId;
    private String subject;
    private String communityTitle;
    private String communityContent;
    private String userId;
    private String userName;
    private LocalDateTime communityRegisterDate;
    private LocalDateTime communityUpdateDate;
    //좋아요 갯수
    private int likeCount;
    private String communityThumbnail;
    private String showContent;
    private int views;
    private String author;

}
