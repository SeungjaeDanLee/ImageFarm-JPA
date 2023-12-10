package com.shop.controller;

import com.shop.dto.MailDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberUpdateFormDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import com.shop.service.MailService;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/register";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("memberErrorMsg", "사용할 수 없는 이메일 입니다.");
            return "member/register";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/register";
        }

        return "redirect:/members/login";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/login";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/login";
    }



    // 회원 이메일로 메일로 임시 비밀번호를 보내는 콘트롤러
    // 회원 비밀번호 찾기
    @GetMapping(value = "/findMember")
    public String findMember(Model model) {
        return "/member/findMember";
    }

    // 비밀번호 찾기시, 임시 비밀번호 담긴 이메일 보내기
    @Transactional
    @PostMapping("/sendEmail")
    public String sendEmail(@RequestParam("memberEmail") String memberEmail) {
        MailDto dto = mailService.createMailAndChangePassword(memberEmail);
        mailService.mailSend(dto);

        return "redirect:/members/login";
    }

    @RequestMapping(value = "/findId", method = RequestMethod.POST)
    @ResponseBody
    public String findId(@RequestParam("memberEmail") String memberEmail) {
        String email = String.valueOf(memberRepository.findByEmail(memberEmail));
        System.out.println("회원 이메일 = " + email);
        if(email == null) {
            return null;
        } else {
            return email;
        }
    }

    @GetMapping("/myPage")
    public String memberInfo(Principal principal, ModelMap modelMap){
        String loginId = principal.getName();
        Member member = memberRepository.findByEmail(loginId);
        modelMap.addAttribute("member", member);

        boolean loggedIn = (loginId != null && !loginId.isEmpty());
        modelMap.addAttribute("loggedIn", loggedIn);

        return "member/myPage";
    }



    // 회원 정보 변경 (POST)
    @PostMapping(value = "/update")
    public String updateMember(@Valid MemberUpdateFormDto memberUpdateFormDto, Model model) {
        model.addAttribute("member", memberUpdateFormDto);
        memberService.updateMember(memberUpdateFormDto);
        return "redirect:/members/myPage";
    }

    @GetMapping("/delete")
    public String deleteMemberPage() {
        return "member/delete"; // 회원 탈퇴 페이지로 리턴
    }

    @PostMapping("/delete")
    public String deleteMember(Model model) {
        // 현재 인증된 사용자 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String loginId = authentication.getName();

            // 여기서 탈퇴 처리 로직을 추가합니다.
            // 탈퇴 처리는 memberService에서 처리하도록 합니다.
            try {
                memberService.deleteMember(loginId);
            } catch (RuntimeException e) {
                // 탈퇴 중에 예외 발생 시 처리 (예: 로그 찍기 등)
                model.addAttribute("errorMessage", e.getMessage());
                return "member/myPage"; // 실패한 경우 다시 사용자의 마이페이지로 리다이렉트
            }

            // 성공한 경우 로그아웃 처리하고 로그인 페이지로 리다이렉트
            SecurityContextHolder.clearContext(); // 로그아웃
            return "redirect:/members/login";
        } else {
            // 로그인되지 않은 사용자는 처리하지 않음
            return "redirect:/members/login";
        }
    }
}