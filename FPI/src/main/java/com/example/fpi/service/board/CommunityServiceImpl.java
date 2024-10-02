package com.example.fpi.service.board;

import com.example.fpi.domain.dto.board.CommunityDTO;
import com.example.fpi.domain.dto.board.CommunityDetailDTO;
import com.example.fpi.domain.dto.board.LikeDTO;
import com.example.fpi.domain.dto.pro.ProSendReqListDTO;
import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.domain.util.PagedResponse;
import com.example.fpi.domain.vo.board.CommunityVO;
import com.example.fpi.domain.vo.board.LikeVO;
import com.example.fpi.mapper.File.FileMapper;
import com.example.fpi.mapper.board.CommentMapper;
import com.example.fpi.mapper.board.CommunityMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    private final CommunityMapper communityMapper;


    //    게시판 리스트,rest
    @Override
    public PagedResponse<CommunityDetailDTO> getCommunityList(int page, int pageSize,String search ,String subject,String sort,CustomOAuth2User user) {
        int startRow = (page - 1) * pageSize;
        int endRow = page * pageSize;

        int totalLists = communityMapper.countCommunity(search,subject);
        int totalPages = (int) Math.ceil((double)totalLists/pageSize);


        List<CommunityDetailDTO> lists = communityMapper.communitySelectAll(startRow,endRow,search,subject,sort);
        for (CommunityDetailDTO list : lists) {
            if(user==null){
                list.setLoginUserId("0");
            }
            else {
                list.setLoginUserId(user.getUserId());
            }
            list.setLikeUse(selectMyLike(list.getLoginUserId(),list.getCommunityId()));
        }
        return new PagedResponse<>(lists, page, totalPages, pageSize, totalLists);
    }



    //    게시판 상세페이지
    @Override
    @Transactional
    public CommunityDetailDTO getCommunityDetail(Long communityId, CustomOAuth2User user, HttpSession session) {
        CommunityDetailDTO community= communityMapper.selectCommunityDetail(communityId);
//        조회수와 댓글수는 ajax
        if(user==null){
            community.setLoginUserId("0");
        }
        else {
            community.setLoginUserId(user.getUserId());
        }
        if(session.getAttribute("loginName") == null){
            community.setLoginName((String) session.getAttribute("proName"));
        }
        else if(session.getAttribute("proName") == null){
            community.setLoginName((String) session.getAttribute("loginName"));
        }
        //        로그인을 안했거나
        if (user ==null || !community.getAuthor().equals(community.getLoginName())) {
//            조회수가 플러스 1이되는 update쿼리문
            communityMapper.plusViews(communityId);
            System.out.println(community);
        }
        community.setLikeUse(selectMyLike(community.getLoginUserId(),communityId));
        return community;
    }

    //    게시판 작성insert
    @Override
    public void saveCommunity(CommunityDTO community) {
        String thumnail = null;
        String content = community.getCommunityContent();

        Long communityId = communityMapper.getSeq();
        community.setCommunityId(communityId);

        // summernote에서 이미지가 여러개 들어갔을때 첫번째 등록한 이미지만 content에서 분리하여 썸네일에 저장해줌
        // 정규표현식 패턴 설정
        String patternString = "<img\\s+src\\s*=\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(patternString);

        // 정규표현식을 이용한 매칭
        Matcher matcher = pattern.matcher(content);

        // 첫 번째 img 태그의 src 속성 값 추출
        if (matcher.find()) {
            thumnail = matcher.group(1);

        } else {
            thumnail = "";
        }
        String showContent = content.replaceFirst("<img\\s+src\\s*=\\s*\"" + Pattern.quote(thumnail) + "\"[^>]*>", "");
        community.setCommunityThumbnail(thumnail);
        community.setShowContent(showContent);
        communityMapper.saveCommunity(community);
//        System.out.println(community);
    }

//    게시글 수정 update
    @Override
    public void updateCommunity(CommunityDTO community) {

        String thumnail = community.getCommunityThumbnail();
        String content = community.getCommunityContent();

        // summernote에서 이미지가 여러개 들어갔을때 첫번째 등록한 이미지만 content에서 분리하여 썸네일에 저장해줌
        // 정규표현식 패턴 설정
        String patternString = "<img\\s+src\\s*=\\s*\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(patternString);

        // 정규표현식을 이용한 매칭
        Matcher matcher = pattern.matcher(community.getCommunityContent());

        // 첫 번째 img 태그의 src 속성 값 추출
        if (matcher.find()) {
            thumnail = matcher.group(1);
        } else {
            thumnail = "";
        }
        String showContent = content.replaceFirst("<img\\s+src\\s*=\\s*\"" + Pattern.quote(thumnail) + "\"[^>]*>", "");

        community.setCommunityThumbnail(thumnail);
        community.setShowContent(showContent);

        communityMapper.editCommunity(CommunityVO.toEntity(community));

    }



    //    게시글 삭제
    @Override
    public void deleteCommunity(Long communityId) {
        communityMapper.deleteCommunity(communityId);
    }

    //    좋아요 기능
    @Override
    public void selectLike(String userId,Long communityId) {
        LikeDTO likeDTO = new LikeDTO();
        CommunityDetailDTO dto =communityMapper.selectCommunityDetail(communityId);
//        해당게시글에 본인의 like가 조회된다면 해당 좋아요 취소,
        Long likeId = communityMapper.selectLike(userId,communityId);
        if(likeId == null || likeId == 0){
            likeDTO.setLikeId(communityMapper.getLikeSeq());
            likeDTO.setUserId(userId);
            likeDTO.setCommunityId(communityId);
            communityMapper.insertLike(LikeVO.toEntity(likeDTO));
//            communityMapper.minusViews(communityId); //페이지 재 실행되면서 카운트 올라가서 코드 추가함
        }
        else {
            communityMapper.deleteLike(likeId);
//            communityMapper.minusViews(communityId);//페이지 재 실행되면서 카운트 올라가서 코드 추가함
        }
        communityMapper.minusViews(communityId);
        System.out.println(likeDTO);

    }

    @Override //게시판 상세페이지에서 컬러효과위해 조회
    public Long selectMyLike(String userId, Long communityId) {
        return communityMapper.selectLike(userId,communityId);
    }

//    @Override
//    public int countViews(Long communityId,CustomOAuth2User user, HttpSession session) {
//        CommunityDetailDTO community= communityMapper.selectCommunityDetail(communityId);
////        로그인되어있는 사람의 이름을 가져옴(헤더이름)
//        if(session.getAttribute("loginName") == null){
//            community.setLoginName((String) session.getAttribute("proName"));
//        }
//        else if(session.getAttribute("proName") == null){
//            community.setLoginName((String) session.getAttribute("loginName"));
//        }
//        //        로그인을 안했거나
//        if (user ==null || !community.getAuthor().equals(community.getLoginName())) {
////            조회수가 플러스 1이되는 update쿼리문
//            communityMapper.plusViews(communityId);
//            System.out.println(community);
//        }
//        return communityMapper.countViews(communityId);
//    }


}
