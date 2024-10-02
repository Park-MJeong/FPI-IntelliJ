package com.example.fpi.controller.board;

import com.example.fpi.domain.dto.board.CommunityDTO;
import com.example.fpi.domain.dto.board.CommunityDetailDTO;
import com.example.fpi.domain.dto.pro.ProReceivedReqListDTO;
import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.domain.util.PagedResponse;
import com.example.fpi.service.board.CommunityService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommunityRestController {

    private final CommunityService communityService;
    @GetMapping("/community/list")
    public ResponseEntity<PagedResponse<CommunityDetailDTO>> getCommunityList(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
                                                                              @RequestParam(defaultValue = "1") int page,
                                                                              @RequestParam(defaultValue = "8") int size,
                                                                              @RequestParam String search,
                                                                              @RequestParam(defaultValue = "") String subject,
                                                                              @RequestParam String sort){

        System.out.println(search);
        System.out.println(subject);
        System.out.println(size+"dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
        return ResponseEntity.ok(communityService.getCommunityList(page, size, search, subject,sort,customOAuth2User));
    }
//
//    //    디테일에서 조회수 바로 반영하기 위해서
//    @GetMapping("/countViews/{communityId}")
//    public ResponseEntity<?> getCountViews(@PathVariable Long communityId, @AuthenticationPrincipal CustomOAuth2User customOAuth2User, HttpSession session) {
//
//
//        int countViews = communityService.countViews(communityId,customOAuth2User,session);
//        return ResponseEntity.ok().body(Collections.singletonMap("countViews", countViews));
//    }

}
