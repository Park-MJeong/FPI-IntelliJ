package com.example.fpi.service.board;

import com.example.fpi.domain.dto.board.CommunityDTO;
import com.example.fpi.domain.dto.board.CommunityDetailDTO;

import com.example.fpi.domain.dto.board.LikeDTO;
import com.example.fpi.domain.dto.pro.ProUploadListDTO;
import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.domain.util.PagedResponse;
import com.example.fpi.domain.vo.board.CommunityVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CommunityService {

    //    게시판리스트 select
    PagedResponse<CommunityDetailDTO> getCommunityList(int page, int pageSize,String search,String subject,String sort,CustomOAuth2User user); //전체게시판,페이징



    CommunityDetailDTO getCommunityDetail(Long communityId, CustomOAuth2User user, HttpSession session); //게시판 상세보기


    void saveCommunity(CommunityDTO community); //게시판 작성
    void updateCommunity(CommunityDTO community); //게시판 수정

    void deleteCommunity(Long communityId); //게시판 삭제
    void selectLike(String userId,Long communityId); //게시판 좋아요기능
    Long selectMyLike(String userId,Long communityId); //좋아요한 게시판인지

//    int countViews(Long communityId,CustomOAuth2User user, HttpSession session);


}

