package com.example.fpi.controller.pay;

import com.example.fpi.domain.oauth.CustomOAuth2User;
import com.example.fpi.service.user.PayCouponService;
import com.example.fpi.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class PayController {

    private final UserService userService;
    private final PayCouponService payCouponService;
//포인트 충전
    @GetMapping("/pay")
    public String pay(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, Model model) {

        String userId = customOAuth2User.getUserId();
        model.addAttribute("user",userService.detailUser(userId));
        model.addAttribute("couponList",payCouponService.couponlist(userId));

        return "/pay/cash";
    }

}
