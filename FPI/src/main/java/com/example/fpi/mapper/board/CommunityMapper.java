package com.example.fpi.mapper.board;

import com.example.fpi.domain.dto.board.CommunityDTO;
import com.example.fpi.domain.dto.board.CommunityDetailDTO;
import com.example.fpi.domain.vo.board.CommunityVO;
import com.example.fpi.domain.vo.board.LikeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityMapper {
    long getSeq();

    List<CommunityDetailDTO> communitySelectAll(int startRow, int endRow,String search,String subject,String sort); //전체 커뮤니티 리스트


    int countCommunity(String search,String subject); //페이징을위한 게시판 수

    CommunityDetailDTO selectCommunityDetail(Long communityId); //게시판 상세보기

    void saveCommunity(CommunityDTO communityDTO); //게시판 작성
    void editCommunity(CommunityVO communityVO);
    void deleteCommunity(Long communityId); //게시글 삭제

    //    좋아요 기능관련
    Long selectLike(String userId,Long communityId); //좋아요한적있는지 확인
    long getLikeSeq(); //좋아요테이블 시퀀스 추가
    void insertLike(LikeVO vo);
    void deleteLike(Long likeId);

    //    조회수
    void plusViews(Long communityId);
    void minusViews(Long communityId);

//    int countViews(Long communityId);

}
