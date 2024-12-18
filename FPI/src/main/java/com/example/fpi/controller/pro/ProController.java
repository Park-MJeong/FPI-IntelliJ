package com.example.fpi.controller.pro;

import com.example.fpi.domain.dto.file.ProCardInfoFileDTO;
import com.example.fpi.domain.dto.file.UserUploadFileDTO;
import com.example.fpi.domain.dto.pro.*;
import com.example.fpi.domain.dto.user.UserAccuseDTO;
import com.example.fpi.domain.dto.user.UserRequestDetailDTO;
import com.example.fpi.domain.dto.user.UserReviewDTO;
import com.example.fpi.domain.dto.user.UserUploadDetailDTO;
import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.mapper.File.FileMapper;
import com.example.fpi.service.pro.ProService;
import com.example.fpi.service.user.PayCouponService;
import com.example.fpi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/pro")
@RequiredArgsConstructor
public class ProController {

    private final ProService proService;
    private final UserService userService;
    private final FileMapper fileMapper;
    private final PayCouponService payCouponService;

    @GetMapping("/requests")
    public String rest() {
        return "pro/req_list/pro_received_req";
    }

    //    전문가가 보낸 요청의 상세보기
    @GetMapping("/proDetail/{proRequestId}")
    public String proReqDetail(@PathVariable("proRequestId") Long proRequestId, Model model) {

        Long proId = userService.selectProIdByProRequestId(proRequestId);
        ProRequestDetailDTO proRequest = proService.selectProReqDetail(proRequestId);
        List<ProCareerInfoListDTO> careerInfo = proService.selectProCareerByReq(proRequestId);
        List<ProCardInfoFileDTO> proCardInfoFiles = fileMapper.selectProCardFileList(proId);
        String userId = proService.selectUserIdByProRequestId(proRequestId);
        System.out.println(userId);

        Long proReviewCnt = proService.selectProReviewCnt(proId);

        model.addAttribute("proReviewCnt", proReviewCnt);

        model.addAttribute("proCardInfoFiles", proCardInfoFiles);
        model.addAttribute("proRequest", proRequest);
        model.addAttribute("careerInfo", careerInfo);
        model.addAttribute("userId", userId);

        return "/pro/req_list/pro_send_req_info";

    }

    //    유저가 보낸 요청의 상세보기
    @GetMapping("/userDetail/{userRequestId}")
    public String userReqDetail(@PathVariable("userRequestId") Long userRequestId, Model model) {

        UserRequestDetailDTO userRequest = userService.selectUserReqDetail(userRequestId);
        String userId = proService.selectUserIdByUserRequestId(userRequestId);

        Long userReviewCnt = userService.selectUserReviewCnt(userId);

        model.addAttribute("userReviewCnt", userReviewCnt);

        model.addAttribute("userRequest", userRequest);
        model.addAttribute("userAccuse", new UserAccuseDTO());

        return "/pro/req_list/pro_received_req_info";

    }

    //    회원 찾기
    @GetMapping("/userFind")
    public String uploadRest() {
        return "pro/userfind/FindUser";
    }

    // 메인카테고리 클릭시 회원 찾기
    @GetMapping("/userFind/{searchType}")
    public String uploadRest2(Model model, @PathVariable("searchType") String searchType) {
        model.addAttribute("searchType", searchType);
        return "pro/userfind/FindUser";
    }

    //     마이페이지 리뷰에서 회원 이름클릭시 회원 찾기
    @GetMapping("/{userId}/userFind")
    public String uploadRest3(Model model, @PathVariable("userId") String userId) {
        String searchUserName = userService.detailUser(userId).getUserName();

        model.addAttribute("searchUserName", searchUserName);
        return "pro/userfind/FindUser";
    }

    //    회원 찾기를 통해 유저가 올리 견적 상세보기
    @GetMapping("/uploadDetail/{userUploadId}")
    public String uploadDetail(@PathVariable("userUploadId") Long userUploadId,
                               @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                               Model model) {
        UserUploadDetailDTO Upload = userService.selectUserUploadDetail(userUploadId);
        System.out.println(userUploadId);

        List<UserUploadFileDTO> userUploadFiles = fileMapper.selectUserUploadFileList(userUploadId);

        String userId = customOAuth2User.getUserId();
        System.out.println(userId);
        Long proId = proService.selectProId(userId);
        ProLocationDTO proLocation = proService.selectProLocation(proId);

        Long checkRequest = proService.checkProRequest(userUploadId, proId);
        Long userReviewCnt = userService.selectUserReviewCnt(userId);

        model.addAttribute("userReviewCnt", userReviewCnt);

        model.addAttribute("userUpload", Upload);

        model.addAttribute("proLocation", proLocation);

        model.addAttribute("userUploadFiles", userUploadFiles);

        model.addAttribute("proRequest", new ProRequestDTO());

        model.addAttribute("checkRequest", checkRequest);

        return "/pro/userfind/userFind_info";
    }

    //    전문가 견적 작성하기
    @GetMapping("/upload")
    public String uploadForm(Model model,
                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        String userId = customOAuth2User.getUserId();
        Long proId = proService.selectProId(userId);
        ProLocationDTO proLocation = proService.selectProLocation(proId);

        ProDetailDTO proDetail = proService.detailPro(proId);

        model.addAttribute("proDetail", proDetail);
        model.addAttribute("proUpload", new ProUploadDTO());
        model.addAttribute("proLocation", proLocation);

        return "/pro/upload/pro_receiveform";
    }

    //    전문가 견적 작성하기
    @PostMapping("/upload")
    public String upload(ProUploadDTO proUpload,
                         @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                         @RequestParam("proUploadFiles") List<MultipartFile> files) {

        if (customOAuth2User != null) {
            String userId = customOAuth2User.getUserId();
            Long proId = proService.selectProId(userId);
            proUpload.setProId(proId);
            System.out.println("proId " + proId);
        }

        System.out.println("proUpload " + proUpload.toString());

        proService.saveProUpload(proUpload, files);

        return "redirect:/main/pro";
    }

    //    전문가가 회원에게 요청 보내기
    @PostMapping("/sendProRequest")
    public String sendRequest(@RequestParam Long userUploadId,
                              ProRequestDTO proRequest,
                              @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        String userId = customOAuth2User.getUserId();
        Long proId = proService.selectProId(userId);
        proRequest.setProId(proId);
        proRequest.setUserUploadId(userUploadId);


        proService.proRequest(proRequest);


        return "redirect:/pro/uploadDetail/" + userUploadId;

    }

    //    전문가가 받은 요청에서 회원의 리뷰 작성하기
    @GetMapping("/userReview/{userRequestId}")
    public String userReviewForm(@PathVariable Long userRequestId,
                                 Model model) {

        String userId = proService.selectUserIdByUserRequestId(userRequestId);

        String userName = userService.getUserName(userId);

        model.addAttribute("userRequestId", userRequestId);
        model.addAttribute("userReview", new UserReviewDTO());
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        System.out.println(userId);
        System.out.println(userName);

        return "/pro/req_list/reviewWrite";
    }

    //    전문가가 보낸 요청에서 리뷰 작성하기
    @GetMapping("/userReview2/{proRequestId}")
    public String userReviewForm2(@PathVariable Long proRequestId,
                                  Model model) {

        String userId = proService.selectUserIdByProRequestId(proRequestId);

        String userName = userService.getUserName(userId);

        model.addAttribute("proRequestId", proRequestId);
        model.addAttribute("userReview", new UserReviewDTO());
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        System.out.println(userId);
        System.out.println(userName);

        return "/pro/req_list/reviewWrite";
    }

    //    전문가가쓴 리뷰 (회원이 받은 리뷰)
    @PostMapping("/userReview")
    public String userReview(UserReviewDTO userReview,
                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                             @RequestParam String userId,
                             @RequestParam Long userRequestId,
                             @RequestParam Long proRequestId) {

        String user = customOAuth2User.getUserId();
        Long proId = proService.selectProId(user);
        userReview.setProId(proId);
        userReview.setUserId(userId);

        if (userRequestId == 0) {
            proService.updateProRequestUserReview(proRequestId);
        } else {
            proService.updateUserRequestUserReview(userRequestId);
        }

        proService.proWriteUserReview(userReview);


        return "redirect:/main/pro";

    }

    //  전문가가 받은 요청에서 삭제
    @PostMapping("/userDetail/delete/{userRequestId}")
    public String deleteUserRequest(@PathVariable Long userRequestId) {
        proService.deleteUserRequest(userRequestId);

        return "redirect:/pro/requests";
    }

    //  전문가가 받은 요청에서 수락하기 업데이트
    @PostMapping("/userDetail/updateAccept/{userRequestId}")
    public String updateAccept(@PathVariable Long userRequestId) {

        proService.updateProAccept(userRequestId);
        return "redirect:/pro/userDetail/" + userRequestId;
    }

    //전문가가 올린글에 회원이 요청 보냈음,캐시교환 이루어짐
    @PostMapping("/userDetail/updateComplete/{userRequestId}")

    public String updateComplete(@PathVariable Long userRequestId, @RequestParam String userId, @RequestParam String userRequestPay) {

        Long proId = userService.selectProIdByUserRequestId(userRequestId);
        Long empCnt = proService.empCount(proId);
        proService.updateEmpCnt(proId, empCnt);

        proService.updateProComplete(userRequestId);


        payCouponService.userRequsestPay(Long.parseLong(userRequestPay), userId, proId); //캐쉬결제하는서비스
        return "redirect:/pro/userDetail/" + userRequestId;
    }

    //  전문가가 받은 요청에서 회원을 신고하기
    @PostMapping("/accuseUser")
    public String accuseUser(@RequestParam Long userRequestId,
                             UserAccuseDTO userAccuse,
                             @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        String user = customOAuth2User.getUserId();
        Long proId = proService.selectProId(user);

        String userId = proService.selectUserIdByUserRequestId(userRequestId);

        userAccuse.setUserId(userId);
        userAccuse.setProId(proId);

        proService.proAccuseUser(userAccuse);
        proService.deleteUserRequest(userRequestId);

        return "redirect:/pro/requests";
    }

    @GetMapping("/proUploadList")
    public String userUpload(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,Model model){
        Long proId= proService.selectProId(customOAuth2User.getUserId());
        model.addAttribute("proId",proId);
        return"/pro/upload/proReceiveList";
    }

}
