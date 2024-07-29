package com.example.fpi.service.user;

import com.example.fpi.domain.dto.board.CommentListDTO;
import com.example.fpi.domain.dto.board.CommunityDTO;
import com.example.fpi.domain.dto.board.UserCommunityListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActiveService {
    //    활동내역 중 게시글 페이징
    List<UserCommunityListDTO> selectUserCommuList(String userId,String myName, int page, int pageSize);
    int countUserCommu(String userId,String myName);
    //    활동내역 중 댓글 페이징
    List<CommentListDTO> selectUserCommentList(String userId, String myName, int startRow, int endRow);
    int countUserComment(String userId,String myName);
}
