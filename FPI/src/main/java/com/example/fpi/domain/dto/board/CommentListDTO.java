package com.example.fpi.domain.dto.board;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentListDTO {
    private Long commentId;
    private Long communityId;
    private String userId;
    private String commentContent;
    private LocalDateTime commentRegisterDate;
    private LocalDateTime commentUpdateDate;
    private String name;
}